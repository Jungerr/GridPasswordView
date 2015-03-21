# GridPasswordView
[ ![Download](https://api.bintray.com/packages/jungerr/maven/gridPasswordView/images/download.svg) ](https://bintray.com/jungerr/maven/gridPasswordView/_latestVersion)

An android password view that looks like the pay password view in wechat app and alipay app.
Apache License 2.0.

##Quick Overview

 - Download [demo-0.2.apk][2]
 - Screenshots

![demo][1]


##Getting Started

 - Add the dependency to your build.gradle.
```
dependencies {
    compile 'com.jungly:GridPasswordView:0.2'
}
```
or Maven:
```
<dependency>
  <groupId>com.jungly</groupId>
  <artifactId>GridPasswordView</artifactId>
  <version>0.2</version>
  <type>aar</type>
</dependency>
```
 - Add the GridPasswordView to your layout.

```xml
    <com.jungly.gridpasswordview.GridPasswordView
        android:id="@+id/pswView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" 
        
        app:textColor="#2196F3"
        app:textSize="25sp"
        app:lineColor="#2196F3"
        app:lineWidth="1dp"
        app:gridColor="#ffffff"
        app:passwordLength="6"
        app:passwordTransformation="$"
        app:passwordType="numberPassword / textPassword / textVisiblePassword / textWebPassword"/>
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

  [1]: http://jungerr.qiniudn.com/gridpasswordview_0.2.gif
  [2]: https://github.com/Jungerr/GridPasswordView/blob/master/demo/demo-0.2.apk
