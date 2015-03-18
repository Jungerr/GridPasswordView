# GridPasswordView
[ ![Download](https://api.bintray.com/packages/jungerr/maven/gridPasswordView/images/download.svg) ](https://bintray.com/jungerr/maven/gridPasswordView/_latestVersion)

An android password view looks like the pay password view on wechat or alipay.
Apache License 2.0.

##Quick Overview

 - Download [demo.apk][2]
 - Screenshots

![demo][1]


##Getting Started

 - Add the dependency to your build.gradle.
 
```
dependencies {
    compile 'com.jungly:GridPasswordView:0.1'
}
```

 - Add the GridPasswordView to your layout.

 ```xml
    <com.jungly.gridpasswordview.GridPasswordView
        android:id="@+id/pswView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" 
        
        app:textColor="#2196F3"
        app:textSize="25sp"
        app:dividerColor="#2196F3"
        app:dividerWidth="1dp"
        app:passwordLength="6"
        app:passwordTransformation="密"
        app:passwordType="numberPassword/textPassword/textVisiblePassword/textWebPassword"/>
 ```


##Contributing

Yes:) If you found a bug, have an idea how to improve library or have a question, please create new issue or comment existing one. If you would like to contribute code fork the repository and send a pull request.

License
---

    Copyright 2015 jungly

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

  [1]: http://jungerr.qiniudn.com/device-2015-03-14-183835.gif
  [2]: https://github.com/Jungerr/GridPasswordView/blob/master/demo/gridpasswordviewdemo-debug.apk
