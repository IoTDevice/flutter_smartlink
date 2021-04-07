#import "FlutterSmartlinkPlugin.h"
#if __has_include(<flutter_smartlink/flutter_smartlink-Swift.h>)
#import <flutter_smartlink/flutter_smartlink-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_smartlink-Swift.h"
#endif

@implementation FlutterSmartlinkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterSmartlinkPlugin registerWithRegistrar:registrar];
}
@end
