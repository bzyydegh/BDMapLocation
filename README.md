# bdmaplocation
基于百度地图API实现定位功能
#AndroidManifest.xml
##添加权限
    <!-- 这个权限用于进行网络定位-->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <!-- 这个权限用于访问GPS定位-->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <!-- 用于访问wifi网络信息，wifi信息会用于进行网络定位-->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <!-- 获取运营商信息，用于支持提供运营商信息相关的接口-->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <!-- 这个权限用于获取wifi的获取权限，wifi信息会用来进行网络定位-->
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
    <!-- 用于读取手机当前的状态-->
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <!-- 写入扩展存储，向扩展卡写入数据，用于写入离线定位数据-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <!-- 访问网络，网络定位需要上网-->
    <uses-permission android:name="android.permission.INTERNET" />
    <!-- SD卡读取权限，用户写入离线定位数据-->
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
##添加app-key
    <!-- meta-data需要写在application中 -->
    <meta-data
       		android:name="com.baidu.lbsapi.API_KEY"
          android:value="**************" /> <!-- 需要申请 -->
##添加服务
<service
     android:name="com.baidu.location.f"
     android:enabled="true"
     android:process=":remote" >
         <intent-filter>
             <action android:name="com.baidu.location.service_v2.2" />
         </intent-filter>
</service>
#build.gradle
##读取lib目录下.so文件
    task nativeLibsToJar(type: Zip, description: "create a jar archive of the native libs") {
        destinationDir file("$projectDir/libs")
        baseName "Native_Libs2"
        extension "jar"
        from fileTree(dir: "libs", include: "**/*.so")
        into "lib"
    }
    tasks.withType(JavaCompile) {
        compileTask -> compileTask.dependsOn(nativeLibsToJar)
    }
		
