package com.iotserv.flutter_smartlink;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.util.Log;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

//smartlink
import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.hiflying.smartlink.v7.MulticastSmartLinker;


import java.util.HashMap;
import java.util.Map;

/** FlutterSmartlinkPlugin */
public class FlutterSmartlinkPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private String ssid;
  private String bssid;
  private String password = null;
  private int timeout = 60;//miao
  private Activity activity;
  private MethodChannel channel;

  protected ISmartLinker mSmartLinker;

  public FlutterSmartlinkPlugin(Activity activity,MethodChannel channel) {
    this.activity = activity;
    this.channel = channel;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_smartlink");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("start")){
      //参数获取
      ssid = call.argument("ssid");
      bssid = call.argument("bssid");
      password = call.argument("password");
      try {
        timeout = call.argument("timeout");
      }catch (Exception e){
        e.printStackTrace();
        timeout = 30;
      }
//    smartLinkVersion == 7
      mSmartLinker = MulticastSmartLinker.getInstance();
//    else
//    mSmartLinker = SnifferSmartLinker.getInstance();
      mSmartLinker.setTimeoutPeriod(timeout * 1000);
      mSmartLinker.setOnSmartLinkListener(
              new OnSmartLinkListener() {
                Map<String, String> ret = new HashMap<String, String>();
                @Override
                public void onLinked(SmartLinkedModule smartLinkedModule) {
                  Log.d("====smartlink===","onLinked");
                  ret.put("result","success");
                  ret.put("Id",smartLinkedModule.getId());
                  ret.put("Mac",smartLinkedModule.getMac());
                  ret.put("Ip",smartLinkedModule.getIp());
                  result.success(ret);
                }

                @Override
                public void onCompleted() {
                  Log.d("====smartlink===","onCompleted");
                  ret.put("result","completed");
                  result.success(ret);
                }

                @Override
                public void onTimeOut() {
                  Log.d("====smartlink===","onTimeOut");
                  ret.put("result","timeout");
                  result.success(ret);
                }
              }
      );
      //开始 smartLink
      try {
        mSmartLinker.start(activity.getApplicationContext(), password,ssid);
      }catch (Exception e) {
        Log.d("====smartlink===",e.getMessage());
        e.printStackTrace();
        Map<String, String> ret = new HashMap<String, String>();
        ret.put("result","exception");
        result.success(ret);
      }


    }else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
