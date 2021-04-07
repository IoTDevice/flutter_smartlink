
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterSmartlink {
  static const MethodChannel _channel =
      const MethodChannel('flutter_smartlink');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future start(String ssid, String pass, String bssid, int timeout) async {
    try {
      Map<String, dynamic> rm = new Map<String, dynamic>.from(
          await _channel.invokeMethod(
              'start', {"ssid": ssid, "password": pass, "bssid": bssid, "timeout": timeout}));
      return rm;
    } catch (err) {
      print("Error===, $err");
      return null;
    }
  }
}
