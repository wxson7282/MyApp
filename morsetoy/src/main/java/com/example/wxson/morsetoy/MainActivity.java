package com.example.wxson.morsetoy;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;

import java.util.regex.*;

public class MainActivity extends AppCompatActivity {
    static IArrayMapForCoding arrayMapForCodingNormal;  //标准编码类
    static IArrayMapForCoding arrayMapForCodingLong;  //长码电报编码类
    static IArrayMapForCoding arrayMapForCodingShort;  //短码电报编码类
    private IMorsePlay<Boolean> morsePlay;                            //播放类
    private IMakeMorseAudioFile<Boolean> makeMorseAudioFile;        //制作音频文件类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取资源
        final Resources res = MainActivity.this.getResources();
        //实例化编码工厂类
        ArrayMapForCodingFactory arrayMapForCodingFactory = new ArrayMapForCodingFactory();
        //取得标准编码类
        arrayMapForCodingNormal = arrayMapForCodingFactory.createArrayMapCodingNormal();
        //取得长码电报编码类
        arrayMapForCodingLong = arrayMapForCodingFactory.createArrayMapCodingLong();
        //取得短码电报编码类
        arrayMapForCodingShort = arrayMapForCodingFactory.createArrayMapCodingShort();

        //实例化播放工厂类
        AbstractMorsePlayFactory morsePlayFactory = new MorsePlayFactory();
        //获得播放类的实例
        morsePlay = morsePlayFactory.createMorsePlay(MorsePlay.class);
        //获得制作音频文件类的实例
        makeMorseAudioFile = new MakeMorseAudioFile();

        //实例化各控件
        final Button btnCode = (Button)findViewById(R.id.btnCode);                                  //编码按钮
        final Button btnPlay = (Button)findViewById(R.id.btnPlay);                                  //播放按钮
        final Button btnMakeFile = (Button)findViewById(R.id.btnMakeFile);                          //制成音频文件按钮
        final EditText editText = (EditText)findViewById(R.id.editText);                            //电文文本
        final TextView textViewMorse = (TextView)findViewById(R.id.textViewMorse);                  //莫尔斯码文本
        final ToggleButton toggleBtnFormat = (ToggleButton)findViewById(R.id.toggleBtnFormat);      //格式按钮
        final ToggleButton toggleBtnLong = (ToggleButton)findViewById(R.id.toggleBtnLong);          //长短码按钮
        final ToggleButton toggleBtnSpeed = (ToggleButton)findViewById(R.id.toggleBtnSpeed);        //播放速度按钮

        //把小写字母转换为大写字母
        editText.setTransformationMethod(new AllCapTransformationMethod());

        //莫尔斯码生成按钮的监听器
        btnCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean isLongCode = toggleBtnLong.isChecked();   //指定长短码
                CoderContext coderContext;            //使用策略模式，封装角色 简化顶层逻辑

                //根据电文格式和长短码，实例化编码类
                if (toggleBtnFormat.isChecked()){
                    coderContext = new CoderContext(new TelegramCoder(isLongCode));
                }
                else {
                    coderContext = new CoderContext(new NormalCoder());
                }
                textViewMorse.setText(coderContext.code(editText.getText().toString()));
            }
        });

        //播放按钮的监听器
        btnPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //设定播放速度
                morsePlay.setSpeed(toggleBtnSpeed.isChecked());
                //根据txtView的内容逐个翻译成音频信号
                for (char chr : textViewMorse.getText().toString().toCharArray()){
                    switch (chr) {
                        case '.':
                            morsePlay.playDi();
                            break;
                        case '_':
                            morsePlay.playDa();
                            break;
                        default:
                            morsePlay.playBlank();
                            break;
                    }
                }
            }
        });

        //电文格式按钮的监听器
        toggleBtnFormat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //电文格式变更时必须清除既存的电文
                editText.setText("");
                textViewMorse.setText("");
                //指定输入电文的格式
                if (toggleBtnFormat.isChecked()){
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    toggleBtnLong.setVisibility(View.VISIBLE);
                }
                else {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    toggleBtnLong.setVisibility(View.INVISIBLE);
                }
            }
        });

        //电文文本的监听器
        editText.addTextChangedListener(new TextWatcher() {
            final StringBuffer sb = new StringBuffer("");    //用于追加空格
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //空方法
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (toggleBtnFormat.isChecked()){
                    //输入电报电文时，每四个数字前加一个空格
                    if (count != 0) {
                        int length = s.length() + 1;
                        if (length % 5 == 0) {
                            sb.delete(0,sb.length());    //清空
                            sb.append(editText.getText());
                            sb.append(' ');              //追加一个空格
                            editText.setText(sb.toString());
                            editText.setSelection(length);
                        }
                    }
                }
                else{
                    //输入标准电文时，应该拦截英数字以外的输入。正则表达式比较方便。
                    String result;
                    Pattern pattern = Pattern.compile(res.getString(R.string.regExpPattern));
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.find(0)){
                        result = matcher.replaceAll("");
                        editText.setText(result);
                        editText.setSelection(result.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //电文变更时，删除以前生成的莫尔斯码
                textViewMorse.setText("");
            }
        });

        //制成音频文件按钮的监听器
        btnMakeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                //用输入电文制成文件名 空格置换为下划线
                String fileName = editText.getText().toString().replace(' ', '_');
                if (!fileName.equals("")) {
                    //创建文件夹
                    String folderName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getPath();
                    File folder = new File(folderName);
                    if (!folder.exists()) {
                        if (!folder.mkdir()){
                            toast = Toast.makeText(MainActivity.this, "文件夹创建失败", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                    }
                    //长度限制在20个字符
                    fileName = fileName.length() > 20 ? fileName.substring(0, 19) : fileName;
                    fileName = folderName + "/" + fileName + ".wav";
                    //指定播放速度
                    makeMorseAudioFile.setSpeed(toggleBtnSpeed.isChecked());
                    //获得运行时文件读写权限
                    verifyStoragePermissions(MainActivity.this);

                    //制成音频文件
                    if (makeMorseAudioFile.makeMorseAudioFile(fileName, textViewMorse.getText().toString())){
                        toast = Toast.makeText(MainActivity.this, "音频文件成功制成，保存在" + fileName, Toast.LENGTH_SHORT);
                    }
                    else {
                        toast = Toast.makeText(MainActivity.this, "音频文件制成失败", Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }
            }
        });
    }

    private void verifyStoragePermissions(Activity activity) {
        final int REQUEST_EXTERNAL_STORAGE = 1;
        final String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};


        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                new AlertDialog.Builder(this)
                        .setMessage("申请WRITE_EXTERNAL_STORAGE权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请WRITE_EXTERNAL_STORAGE权限
                                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                            }
                        })
                        .show();
            }
            else {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                Toast toast = Toast.makeText(MainActivity.this, "已经申请WRITE_EXTERNAL_STORAGE权限", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else {
            Toast toast = Toast.makeText(MainActivity.this, "已经获得WRITE_EXTERNAL_STORAGE权限", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                Toast toast;
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    toast = Toast.makeText(MainActivity.this, "权限被用户同意，可以去放肆了。", Toast.LENGTH_SHORT);
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    toast = Toast.makeText(MainActivity.this, "权限被用户拒绝了，洗洗睡吧。", Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        }
    }

}
