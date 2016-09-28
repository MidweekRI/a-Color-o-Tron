package my.drafts.wifi.leds;

import android.app.Activity;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import android.view.View;
import android.widget.EditText;
import android.os.Bundle;
import android.net.Uri;
import android.widget.ImageView; 
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.Socket;


public class Main extends Activity
{
    Bitmap bmpToSend;
    int port = 4567;
    int w = 8;
    int h = 5;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void browseImage(View view) {
        Log.i("MainActivity", "browseImage");
        Intent pickFile = new Intent( Intent.ACTION_GET_CONTENT );
        pickFile.setType("image/*");
        startActivityForResult(pickFile, PICK_FILE_REQUEST);

    }

    public byte[] copyBuffer(){

        byte[] buf = new byte[3*w*h];
        String result = "";
        for(int x = 0;x < w; x++){
            int L = x*w;
            for(int y = 0; y < h; y++ ){
                int px = bmpToSend.getPixel(x,y);
                //result += String.format("%6X", px);
                   // String.format("%s",Integer.toHexString(px));
                byte b;
                b =(byte) px;
                result += String.format("%01X", (b>>4)&0x0f);
                b = (byte)( px >> 8);
                result += String.format("%01X", (b>>4)&0x0f);
                b = (byte)( px >> 16);
                result += String.format("%01X", (b>>4)&0x0f);
            }
        }

        Log.i("build_buf", String.format("(%d) %s", result.length(), result));
        msgBox(result);
        buf = result.getBytes();
        return buf;
    }
    public void sendImage(View view) {
        //ImageSender.Send(data.data);
        if(bmpToSend != null){
            try{
                byte[] buf = copyBuffer();
                EditText ui = (EditText) findViewById(R.id.edit_ip);
                String ip = ui.getText().toString();
                Log.i("sending", String.format("%s:%d",ip,port));
                Socket s = new Socket(ip,port);
                OutputStream os = new FileOutputStream("/sdcard/test.txt");
                if(s.isConnected())
                  os = s.getOutputStream();
                byte[] cmd = "INS ".getBytes();
                os.write(cmd);
                os.write(buf);
                byte[] end = {0x0d,0x0a};
                os.write(end);
                os.close();
            }catch(Exception ex){
                Log.e("sending", ex.toString());
            }
            
        } else {
            msgBox(Integer.toHexString(0x00ffab00));
        }
    }

    void msgBox(String msg){
        Context ctx = getApplicationContext(); 
        Toast.makeText(ctx,msg, Toast.LENGTH_LONG).show();
    }

    void previewSelectedImage(Intent data)
    {
        Uri uri = data.getData();
        String msg = getString(R.string.img_selected) + uri.getPath();
        msgBox(msg);

        try{
            Log.i("image_url", data.toString());
            w = getValue(R.id.edit_cols);
            h = getValue(R.id.edit_rows);
            Log.i("preview_image",
                    String.format("(WxH): %d x %d",w,h));

            ImageView img = (ImageView) findViewById(R.id.edit_img);
            //img.setImageURI (uri);
            InputStream is = getContentResolver().openInputStream(uri);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            bmpToSend = Bitmap.createScaledBitmap(bmp,w,h,true);
            img.setImageBitmap (bmpToSend);
        }
        catch(Exception ex)
        {
            Log.e("preview_image", ex.toString());
        }
    }

    int getValue(int resId){
        EditText ui = (EditText) findViewById(resId);
        int val = Integer.valueOf(ui.getText().toString());
        return val;
    }

    static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
        data) {
        Log.i("MainActivity",
                String.format("onactivityresult:"+data.toString()));
        // Check which request we're responding to
        if (requestCode == PICK_FILE_REQUEST) {
                // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                previewSelectedImage(data);
           }
        }
    }
}
