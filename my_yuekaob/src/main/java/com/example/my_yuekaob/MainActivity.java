package com.example.my_yuekaob;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.bawei.xlistviewlibrary.XListView;
import com.google.gson.Gson;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import adapter.MyAdapter;
import bean.MyBean;
import utils.Myurl;

public class MainActivity extends AppCompatActivity {
    private String path = Environment.getExternalStorageDirectory()+ File.separator;
    private XListView mXLV;
    private List<MyBean.AppBean> mAppBeanList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initview();
        //初始化数据
        initdata();
    }
    private void initview() {
        //找控件
        mXLV = (XListView) findViewById(R.id.xlistview);
    }
    //初始化数据
    private void initdata() {
        RequestParams params = new RequestParams(Myurl.URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                String str = result.substring(0, result.length() - 1);
                //解析数据
                Gson gson = new Gson();
                MyBean json = gson.fromJson(str, MyBean.class);
                //得到数据集合
                mAppBeanList = json.getApp();
                //给xlistview设置适配器
                MyAdapter adapter = new MyAdapter(mAppBeanList,MainActivity.this);
                mXLV.setAdapter(adapter);
                //给xlistview设置条目点击监听
                mXLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Toast.makeText(MainActivity.this,"点击了",Toast.LENGTH_LONG).show();
                        //写一个Dialog
                        new AlertDialog.Builder(MainActivity.this)
                        .setTitle("网络选择")
                        .setIcon(R.mipmap.ic_launcher)
                                //创建对话框
                        .setSingleChoiceItems(new String[]{"wifi", "手机流量"}, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               switch (which){
                                   case 0:
                                       new AlertDialog.Builder(MainActivity.this)
                                               .setTitle("版本更新")
                                               .setMessage("现在检查到新版本，是否更新?")
                                               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       dialog.dismiss();
                                                   }
                                               })
                                               .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                   private ProgressDialog mProgressdialog;

                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       mProgressdialog = new ProgressDialog(MainActivity.this);
                                                       RequestParams params = new RequestParams(mAppBeanList.get(position).getUrl());
                                                       params.setSaveFilePath(path+mAppBeanList.get(position).getName()+".apk");
                                                       x.http().post(params, new ProgressCallback<File>() {
                                                           @Override
                                                           public void onSuccess(File result) {
                                                               Toast.makeText(MainActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                                                               Intent intent = new Intent(Intent.ACTION_VIEW);
                                                               intent.setDataAndType(Uri.fromFile(result),"application/vnd.android.package-archive");
                                                               startActivity(intent);
                                                           }

                                                           @Override
                                                           public void onError(Throwable ex, boolean isOnCallback) {
                                                               Toast.makeText(MainActivity.this,"下载失败"+ex.getMessage(),Toast.LENGTH_SHORT).show();
                                                               Log.i("rjz",ex.getMessage());
                                                               if (ex instanceof HttpException) { //网络错误
                                                                   HttpException httpEx = (HttpException) ex;
                                                                   int responseCode = httpEx.getCode();
                                                                   String responseMsg = httpEx.getMessage();
                                                                   String errorResult = httpEx.getResult();

                                                               } else {

                                                               }
                                                           }

                                                           @Override
                                                           public void onCancelled(CancelledException cex) {

                                                           }

                                                           @Override
                                                           public void onFinished() {

                                                           }

                                                           @Override
                                                           public void onWaiting() {

                                                           }

                                                           @Override
                                                           public void onStarted() {

                                                           }

                                                           @Override
                                                           public void onLoading(long total, long current, boolean isDownloading) {
                                                               mProgressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                               mProgressdialog.setMessage("下载中");
                                                               mProgressdialog.show();
                                                               mProgressdialog.setMax((int) total);
                                                               mProgressdialog.setProgress((int) current);
                                                           }
                                                       });

                                                   }
                                               }).show();

                                       break;
                                   case 1:
                                       Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                       startActivity(intent);
                                       break;
                               }
                            }
                        }).show();

                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
