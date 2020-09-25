package com.example.test2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import jp.co.dnp.photoprintlib.DNPPhotoPrint;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DNPPhotoPrint mPrint = new DNPPhotoPrint(this);//1.创建DNP打印实例

        int[][] portArray = {{5, 1}};
        int portNum = mPrint.GetPrinterPortNum(portArray);//2.指定端口号,这将返回当前连接的设备和设备ID。如果成功，则返回连接的打印机数。
        Log.d("aaaaaaa",portNum+"");
        Toast.makeText(this, "aaaa"+portNum, Toast.LENGTH_SHORT).show();
        int[][] array = new int[4][2];
        int num = mPrint.GetPrinterPortNum(array);
        if (num < 0) {       // error
        }
        // In order of the device and unit ID
        // array[0][0]   Device type
        // array[0][1]   Unit ID number
        // array[1][0]   Device type
        // array[1][1]   Unit ID number


        //mPrint.SendImageData();
        byte[] rgbData = getBytesByFile("");
        if (mPrint.SetMediaSize(portNum, DNPPhotoPrint.CSP_PCx2)) {//设置输出界面大小
            if (!mPrint.SendImageData(portNum, rgbData, 0, 0, 2048, 1380)) {
                // error
            }
        }
    }

    //文件转byte[]
    public static byte[] getBytesByFile(String filePath) {
        try {
            File file = new File(filePath);
            //获取输入流
            FileInputStream fis = new FileInputStream(file);

            //新的 byte 数组输出流，缓冲区容量1024byte
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            //缓存
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            //改变为byte[]
            byte[] data = bos.toByteArray();
            //
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}