package com.example.wxson.morsetoy;

import android.Manifest;
import android.content.res.Resources;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.List;
import java.util.regex.*;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 2020/2/18  refactoring
 */
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    protected static final String TAG = "MainActivity";
    static IArrayMapForCoding arrayMapForCodingNormal;  //标准编码类
    static IArrayMapForCoding arrayMapForCodingLong;  //长码电报编码类
    static IArrayMapForCoding arrayMapForCodingShort;  //短码电报编码类
    private IMorsePlayer<Boolean> morsePlay;                            //播放类
    private IMorseAudioFileMaker<Boolean> makeMorseAudioFile;        //制作音频文件类
    private Button btnMakeFile;                          //制成音频文件按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取资源
        final Resources res = MainActivity.this.getResources();
        //实例化编码工厂类
        ArrayMapForCodingFactory arrayMapForCodingFactory = new ArrayMapForCodingFactory(res);
        //取得标准编码类
        arrayMapForCodingNormal = arrayMapForCodingFactory.createArrayMapCodingNormal();
        //取得长码电报编码类
        arrayMapForCodingLong = arrayMapForCodingFactory.createArrayMapCodingLong();
        //取得短码电报编码类
        arrayMapForCodingShort = arrayMapForCodingFactory.createArrayMapCodingShort();

        //获得播放类的实例
        morsePlay = MorsePlayerFactory.createMorsePlay(this);
        //获得制作音频文件类的实例
        makeMorseAudioFile = new MorseAudioFileMaker(res);

        //实例化各控件
        final Button btnCode = findViewById(R.id.btnCode);                                  //编码按钮
        final Button btnPlay = findViewById(R.id.btnPlay);                                  //播放按钮
        final EditText editText = findViewById(R.id.editText);                            //电文文本
        final TextView textViewMorse = findViewById(R.id.textViewMorse);                  //莫尔斯码文本
        final ToggleButton toggleBtnFormat = findViewById(R.id.toggleBtnFormat);      //格式按钮
        final ToggleButton toggleBtnLong = findViewById(R.id.toggleBtnLong);          //长短码按钮
        final ToggleButton toggleBtnSpeed = findViewById(R.id.toggleBtnSpeed);        //播放速度按钮
        btnMakeFile = findViewById(R.id.btnMakeFile);

        //把小写字母转换为大写字母
        editText.setTransformationMethod(new UppercaseTransformationMethod());

        //申请权限
        requestStoragePermission();

        //莫尔斯码生成按钮的监听器
        btnCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean isLongCode = toggleBtnLong.isChecked();   //指定长短码
                CoderContext coderContext;            //使用策略模式，封装角色 简化顶层逻辑
                //根据电文格式和长短码，实例化编码类
                if (toggleBtnFormat.isChecked()){
                    coderContext = new CoderContext(new MorseCoder(isLongCode ? arrayMapForCodingLong : arrayMapForCodingShort));
                }
                else {
                    coderContext = new CoderContext(new MorseCoder(arrayMapForCodingNormal));
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
            final StringBuffer sb = new StringBuffer();    //用于追加空格
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
                //用输入电文制成文件名 空格置换为下划线
                String fileName = editText.getText().toString().replace(' ', '_');
                if (!fileName.equals("")) {
                    //创建文件夹
                    String folderName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getPath();
                    File folder = new File(folderName);
                    if (!folder.exists()) {
                        if (!folder.mkdir()){
                            Toast.makeText(MainActivity.this, "文件夹创建失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    //长度限制在20个字符
                    fileName = fileName.length() > 20 ? fileName.substring(0, 19) : fileName;
                    fileName = folderName + "/" + fileName + ".wav";
                    //指定播放速度
                    makeMorseAudioFile.setSpeed(toggleBtnSpeed.isChecked());
                    //获得运行时文件读写权限
//                    verifyStoragePermissions(MainActivity.this);

                    //制成音频文件
                    if (makeMorseAudioFile.makeMorseAudioFile(fileName, textViewMorse.getText().toString())){
                        Toast.makeText(MainActivity.this, "音频文件成功制成，保存在" + fileName, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "音频文件制成失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void requestStoragePermission(){
        Log.i(TAG, "requestStoragePermission");
        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, permission)) {
            // Already have permission, do the thing
            Log.i(TAG, "已获取写权限");
            btnMakeFile.setEnabled(true);
        } else {
            // Do not have permissions, request them now
            Log.i(TAG, "申请写权限");
            //requestCode
            int REQUEST_STORAGE_PERMISSION = 1;
            EasyPermissions.requestPermissions(this, getString(R.string.storage_rationale),
                    REQUEST_STORAGE_PERMISSION, permission);
        }
    }

    //下面两个方法是实现EasyPermissions的EasyPermissions.PermissionCallbacks接口
    //分别返回授权成功和失败的权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.i(TAG, "onPermissionsGranted");
        Log.i(TAG, "获取权限成功" + perms);
        Toast toast = Toast.makeText(MainActivity.this, "获取权限成功", Toast.LENGTH_SHORT);
        toast.show();
        btnMakeFile.setEnabled(true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.i(TAG, "onPermissionsDenied");
        Log.i(TAG, "获取权限失败，保存按钮不可用" + perms);
        Toast toast = Toast.makeText(MainActivity.this, "获取权限失败，保存按钮不可用", Toast.LENGTH_SHORT);
        toast.show();
        btnMakeFile.setEnabled(false);
    }
}
