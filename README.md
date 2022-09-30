# ktils
kotlin utils

## 导入依赖

在根 build.gradle 或 settings.gradle中加入
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
在模块 build.gradle中加入
```groovy
dependencies {
    implementation 'com.github.tosmo5:ktils:Tag'
}
```
