# ktils

kotlin utils

## 导入依赖

在根 build.gradle 或 settings.gradle中加入

build.gradle

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

build.gradle.kts

```kotlin
repositories {
    maven { setUrl("https://jitpack.io") }
}
```

在模块 build.gradle中加入

```groovy
dependencies {
    implementation 'com.github.tosmo5:ktils:0.3.3'
}
```

## 模块

| 类                      | 功能简介        |
|------------------------|-------------|
| Holder                 | 可对自由地绑定接口的实现 |
| IdWorker               | 唯一Id生成器     |
| Refecter               | 反射器         |
| TimeUtils              | 时间工具类       |
| BaseMapper             | 实体类标准接口     |
| ParamReplacer          | 文本参数替换      |
| SegmentationCalculator | 分段计算器       |
| TypeMap            | 分类存储的映射集合 |
