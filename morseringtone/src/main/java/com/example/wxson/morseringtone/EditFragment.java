package com.example.wxson.morseringtone;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    Button btnCoding;
    Button btnPlay;
    ToggleButton btnHighSpeed;
    TextView txtView;
    EditText editTxt;
    String[] charArray;         //字符数组
    String[] morseCodeArray;   //莫尔斯码数组
    private ArrayMapForCoding arrayMapForCoding;  //编码类
    IMorseCode<Boolean> morseCode;       //morseCode类的实例

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);
        //把小写字母转换为大写字母
        ((EditText)rootView.findViewById(R.id.edit_text)).setTransformationMethod(new AllCapTransformationMethod());
        //取得TextView EditText
        txtView = (TextView) rootView.findViewById(R.id.textView);
        editTxt = (EditText) rootView.findViewById(R.id.edit_text);
        //莫尔斯电码生成按钮的监听器 匿名内部类
        btnCoding = (Button)rootView.findViewById(R.id.btnCoding);
        btnCoding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将输入字符逐一转换为莫尔斯电码
                String displayCodes = "";
                for (char chr : editTxt.getText().toString().toUpperCase().toCharArray()){
                    if (chr == ' '){
                        displayCodes += " ";
                    }
                    else {
                        displayCodes += arrayMapForCoding.GetMorseCode(chr);
                    }
                    displayCodes += " ";
                }
                txtView.setText(displayCodes);
            }
        });
        //播放按钮的监听器 匿名内部类
        btnPlay = (Button)rootView.findViewById(R.id.btnPlay);
        btnHighSpeed = (ToggleButton)rootView.findViewById(R.id.btnHighSpeed);
        //监听器
        btnPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //设定播放速度
                morseCode.setSpeed(btnHighSpeed.isChecked());
                //根据txtView的内容逐个翻译成音频信号
                for (char chr : txtView.getText().toString().toCharArray()) {
                    switch (chr) {
                        case '.':
                            morseCode.playDi();
                            break;
                        case '_':
                            morseCode.playDa();
                            break;
                        default:
                            morseCode.playBlank();
                            break;
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取字符数组和莫尔斯码数组
        Resources res =getResources();
        charArray = res.getStringArray(R.array.characters);
        morseCodeArray = res.getStringArray(R.array.morse_code);
        //实例化编码类
        arrayMapForCoding = new ArrayMapForCoding(charArray, morseCodeArray);
        //声明morseCodeFactory
        AbstractMorseCodeFactory morseCodeFactory = new MorseCodeFactory();
        //获得IMorseCode的实例
        morseCode = morseCodeFactory.createMorseCode(MorseCode.class);
    }

}
