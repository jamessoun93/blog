# permission_handler 패키지 필요 설정들

이거 때문에 넘 고통받았기 때문에 적어둔다.

## iOS 알림 설정

### 1. Podfile 수정

`Podfile`에 `post_install`에 `PERMISSION_NOTIFICATIONS=1` 추가해야 한다.

```podfile
post_install do |installer|
  installer.pods_project.targets.each do |target|
    flutter_additional_ios_build_settings(target)
    target.build_configurations.each do |config|
      config.build_settings.delete 'IPHONEOS_DEPLOYMENT_TARGET'
      # Begin of the permission_handler configuration
      config.build_settings['GCC_PREPROCESSOR_DEFINITIONS'] ||= [
        '$(inherited)',

        ## dart: PermissionGroup.notification
        'PERMISSION_NOTIFICATIONS=1',
      ]
      # End of the permission_handler configuration
    end
  end
end
```

다른 permission을 추가해야 한다면 찾아서 추가해줘야 한다.

### 2. Info.plist 동기화

필요한 권한을 Podfile 에 작성했다면, 해당 권한을 요구하는 설명값도 반드시 Info.plist 파일에 작성해줘야 한다.

```plist
<key>PermissionGroupNotification</key>
<string>OOO 앱은 서비스 알림을 제공하기 위해서 디바이스 알림 권한을 필요로 합니다.</string>
```

### 3. Pod install

flutter 프로젝트 내 ios 폴더로 이동한 뒤,

```sh
pod install
```

### 4. Run

다시 flutter
Run flutter clean.
Run flutter run.
