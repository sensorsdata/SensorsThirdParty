Pod::Spec.new do |s|
  s.name = 'SensorsThirdParty'
  s.version = '0.0.3'
  s.summary = 'The official iOS SDK of Sensors ThirdParty.'
  s.homepage = 'http://www.sensorsdata.cn'
  s.license = { :type => 'Apache 2.0', :file => 'LICENSE' }
  s.source = { :git => 'https://github.com/sensorsdata/SensorsThirdParty.git', :tag => 'v' + s.version.to_s}
  s.author = 'Sensors Data'
  s.ios.deployment_target = '9.0'
  s.osx.deployment_target = '10.11'
  s.frameworks = 'Foundation'
  s.dependency 'SensorsAnalyticsSDK'
  s.default_subspec = 'Core'

  base_dir = 'iOS/SensorsThirdParty/SensorsThirdParty/'

  s.subspec 'Core' do |c|
    c.source_files = base_dir + 'Source/Core/*.{h,m}', base_dir + 'Source/include/*.{h,m}',
    c.public_header_files = base_dir + 'Source/include/*.h'
  end
  s.subspec 'AppsFlyer' do |d|
    s.static_framework = true
    d.dependency 'AppsFlyerFramework', '>=6.9.2'
    d.dependency 'SensorsThirdParty/Core'
    d.source_files = base_dir + 'Source/AppsFlyer/*.{h,m}'
    d.project_header_files = base_dir + 'Source/AppsFlyer/*.h'
  end
  s.subspec 'AppsFlyerDynamic' do |e|
    e.dependency 'AppsFlyerFramework/Dynamic', '>=6.9.2'
    e.dependency 'SensorsThirdParty/Core'
    e.source_files = base_dir + 'Source/AppsFlyer/*.{h,m}'
    e.project_header_files = base_dir + 'Source/AppsFlyer/*.h'
  end
  s.subspec 'Adjust' do |f|
    f.dependency 'Adjust', '>=4.33.4'
    f.dependency 'SensorsThirdParty/Core'
    f.source_files = base_dir + 'Source/Adjust/*.{h,m}'
    f.project_header_files = base_dir + 'Source/Adjust/*.h'
  end
end
