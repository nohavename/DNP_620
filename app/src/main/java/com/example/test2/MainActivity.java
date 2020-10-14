package com.example.test2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import jp.co.dnp.photoprintlib.DNPPhotoPrint;

public class MainActivity extends AppCompatActivity {

    private DNPPhotoPrint mPrint;
    private byte[] rgbData;
    private int portNum;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageview);

        new Thread(new Runnable() {
            @Override
            public void run() {
                rgbData = getBytesByFile("");
            }
        }).start();
    }

    Bitmap bitmap;

    //文件转byte[]
    public byte[] getBytesByFile(String filePath) {
        try {
            //File file = new File(filePath);
            //获取输入流
            // FileInputStream fis = new FileInputStream(file);

            InputStream fis = getAssets().open("test4.jpg");

            bitmap = BitmapFactory.decodeStream(fis);
            bitmap = MyUtil.zoomImg(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);//设置图片缩放
            bitmap = MyUtil.rotateImg(bitmap, 90);//旋转90度
            bitmap = MyUtil.createBitmap(bitmap);//构建一个宽1824，高1216的图片

            imageView.setImageBitmap(bitmap);

            return MyUtil.getRGBByBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clickPic(View view) {
        mPrint = new DNPPhotoPrint(this);
        int[][] portArray = new int[1][2];
        portArray[0][0] = 20;
        portArray[0][1] = 1;
        //2.指定端口号,这将返回当前连接的设备和设备ID。如果成功，则返回连接的打印机数。
        portNum = mPrint.GetPrinterPortNum(portArray);

        testLog();

        if (mPrint.SetMediaSize(0, DNPPhotoPrint.CSP_PC * 2)) {//设置输出界面大小
            if (!mPrint.SetResolution(0, DNPPhotoPrint.RESOLUTION300)) {
                System.out.println("SetResolutionerror");
            }
            //mPrint.SetCutterMode(0, DNPPhotoPrint.CUTTER_MODE_2INCHCUT);//切割,图片中间处切割
            mPrint.SetOvercoatFinish(portNum, DNPPhotoPrint.OVERCOAT_FINISH_MATTE1);//设置界面光泽

            if (!mPrint.SendImageData(0, rgbData, 20, 20, bitmap.getWidth(), bitmap.getHeight())) {
                // error
                System.out.println("errorrrrr");
            } else {
                System.out.println("success");
                if (!mPrint.PrintImageData(0)) {
                    System.out.println("success2");
                }
            }
        } else {
            System.out.println("++++++++++++++++");
        }

        testLog();
    }

    private void testLog() {
        System.out.println("1111111111:portNum:" + portNum      //打印机连接数量
                + "\nGetStatus:" + mPrint.GetStatus(0)       //错误状态
                + "\nGetMedia:" + mPrint.GetMedia(0, new char[256])//接收缓冲区中的媒体代码 6
                + "\nGetCounterA:" + mPrint.GetCounterA(0)//寿命计数器，已打印数量 1820  1827 --1828  1828
                + "\nGetCounterB:" + mPrint.GetCounterB(0)//1820  1827  --1828  1828
                + "\nGetCounterL:" + mPrint.GetCounterL(0)//1864  1871  --1872  1872
                + "\nGetCounterM:" + mPrint.GetCounterM(0)//0  0   --0  0
                + "\nGetCounterP:" + mPrint.GetCounterP(0)//0  0   --1  1
                + "\nGetCounterMatte:" + mPrint.GetCounterMatte(0)//0  0  --0
                + "\nGetFirmwVersion:" + mPrint.GetFirmwVersion(0, new char[256])//缓冲区中的打印机版本信息  20  20  --20  20
                + "\nGetInitialMediaCount:" + mPrint.GetInitialMediaCount(0)//返回媒体计数  200  200
                + "\nGetMediaCounter:" + mPrint.GetMediaCounter(0)//剩余张数，如果此值为0，则表示功能区结束，但在此之前运行功能区检查  189  182  --181  181  --656
                + "\nGetMediaCounterH:" + mPrint.GetMediaCounterH(0)//387  364  --362  360
                + "\nGetMediaLotNo:" + mPrint.GetMediaLotNo(0, new char[256])//打印机将返回存储在介质的RFID标签中的信息  //20  20
                + "\nGetUSBSerialEnable:" + mPrint.GetUSBSerialEnable(0)//USB iSerialNumber可用性设置  1  ·
                + "\nGetRewindMode:" + mPrint.GetRewindMode(0)//返回倒带模式  0  0
                + "\nGetSerialNo:" + mPrint.GetSerialNo(0, new char[256])//返回打印机序列号  12  12
                + "\nGetFreeBuffer:" + mPrint.GetFreeBuffer(0));    //返回可用图像缓冲区的数量  2  2
    }

}