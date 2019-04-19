package com.abhikr.abhikr.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abhikr.abhikr.Fire_Push.Push_getToken;
import com.abhikr.abhikr.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class Product_loadbar extends Fragment {

    private ProgressBar progressbar;
    private TextView updateProgressBar;
    private int progressCount;
    private Handler progressHandler = new Handler();
    private static Handler progressbarHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View abhi=inflater.inflate(R.layout.fragment_product_loadbar, container, false);
        progressbar = (ProgressBar) abhi.findViewById(R.id.progressBar);
        updateProgressBar = (TextView) abhi.findViewById(R.id.progressinfo);

        progressbarHandler = new Handler() {
            public void handleMessage(Message msg) {
                String passedProgress = msg.getData().getString("passed");
                if (null != passedProgress) {
                    progressbar.setProgress(progressCount);
                    if(progressCount>90)
                    {
                       /* gerg_explist abhi=new gerg_explist();
                        FragmentManager manager=getFragmentManager();
                        manager.beginTransaction().replace(R.id.container,abhi,"test").addToBackStack(null).commit();
                        */
                       try {
                           Push_getToken abhi = new Push_getToken();
                           FragmentManager manager = getFragmentManager();
                           manager.beginTransaction().replace(R.id.container, abhi, "test").addToBackStack(null).commit();
                       }
                       catch (Exception e)
                       {
                           e.printStackTrace();
                       }

                        //Toast.makeText(getActivity(), "hello abhi ", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getActivity(), Main2Activity.class));
                    }
                    updateProgressBar.setText("Current Progress - " + progressCount);
                    //updateProgressBar.setText(progressCount);
                    //Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                    /*gerg_explist abhi=new gerg_explist();
                    FragmentManager manager=getFragmentManager();
                    manager.beginTransaction().replace(R.id.container,abhi,"test").addToBackStack(null).commit();
*/
                }

            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (progressCount < 100) {
                    try {
                        Thread.sleep(500);
                        progressCount += 20;

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Message currentProgress = progressbarHandler.obtainMessage();
                    Bundle progressBundle = new Bundle();
                    progressBundle.putString("passed", String.valueOf(progressCount));

                    currentProgress.setData(progressBundle);
                    progressbarHandler.sendMessage(currentProgress);



                }
            }
        }).start();
        return abhi;
    }


}

/*
downloadButton.setOnClickListener(new OnClickListener(){
        String imagePath = "https://inducesmile.com/wp-content/uploads/2014/12/bg.jpg";
@Override
public void onClick(View arg0) {
        DownloadImageTask downloadImageTask = new DownloadImageTask();
        downloadImageTask.execute(imagePath);
        }
        });
        }

private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
    @Override
    protected Bitmap doInBackground(String... params) {
        // TODO Auto-generated method stub
        String getImageUrl = params[0];
        Bitmap externalDownloadImage = downloadImage(getImageUrl);
        return externalDownloadImage;
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        progressbar.setVisibility(ProgressBar.GONE);
        if(null != result){
            downloadImage.setImageBitmap(result);
        }
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressbar.setVisibility(ProgressBar.VISIBLE);
    }

    private Bitmap downloadImage(String imageUrl){
        Bitmap bitmap = null;
        URL downloadUrl;
        try {
            downloadUrl = new URL(imageUrl);
            URLConnection connection = downloadUrl.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("There is error getting the image from the server", e.getMessage());
        }
        return bitmap;
    }
}*/
