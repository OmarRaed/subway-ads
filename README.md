# Subway Ads
[![](https://jitpack.io/v/OmarRaed/subway-ads.svg)](https://jitpack.io/#OmarRaed/subway-ads)

An android Library used to easily implement ads for the Subway E-ticketing Android App

---

### Download using Gradle

Add this in your root `build.gradle` at the end of `repositories` in `allprojects` section:
```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}
```

Then add this dependency to your **module-level** `build.gradle` in `dependencies` section:
```groovy
implementation 'com.github.OmarRaed:subway-ads:1.2'
```

---

## How To Use

declare a lateinit var that will holds the ad service

```kotlin
private lateinit var adService: AdService
```

then initialize the AdService and request a new ad

```kotlin
adService = AdService(application, "token goes here...")
adService.requestAd()
```

and whenever you want to show add use call *playAd()* method

```kotlin
adService.playAd()
```

you can add optional listener to trigger when ad is ready

```kotlin
adService.requestAd(object : AdMetaApiListener {
    override fun onAdReady() {
        //ad is ready to be played
    }
})
```

---

**Don't forget to cancel requesting ads job whenever you Destroy the activity**

```kotlin
    override fun onDestroy() {
        super.onDestroy()
        adService.cancelJob()
    }
```

or from the view model of you are following **MVVM**

```kotlin
    override fun onCleared() {
        super.onCleared()
        adService.cancelJob()
    }
```
---
### Notes :

- *'' You only need to call *requestAd()* once as it recall itself after each time you play an ad ''*

- *'' You can check if there is an ad is ready to be shown by calling *isAdReady()*, but no need for that as it's checked internally before playing any ad ''*



