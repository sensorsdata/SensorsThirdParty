Pod::Spec.new do |s|
  s.name = 'SensorsThirdParty'
  s.version = '0.0.1'
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
    s.static_framework = true
    c.dependency 'AppsFlyerFramework', '>=6.9.2'
    c.source_files = base_dir + 'Source/**/*.{h,m}'
    c.public_header_files = base_dir + 'Source/include/*.h'
  end
  s.subspec 'Dynamic' do |b|
    b.dependency 'AppsFlyerFramework/Dynamic', '>=6.9.2'
    b.source_files = base_dir + 'Source/**/*.{h,m}'
    b.public_header_files = base_dir + 'Source/include/*.h'
  end
end
