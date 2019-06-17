# Organa

Utility Android mobile app for renaming audio files.

[![CircleCI](https://circleci.com/gh/gafful/organa-android.svg?style=svg)](https://circleci.com/gh/gafful/organa-android)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)


# Motivation

Finding audio files by name after receiving it through WhatsApp was constantly a headache. Same thing happens with some audio files downloaded over the web from some websites. Besides the file names being messed up, I'd rather my audio files were organised by artistes and their albums. [Long story](https://medium.com/gafful/...) short, I decided to write this app to "organise" my audio files.

<Screenshots>

## Features
* Read audio metadata
* 
Checkout the wishlist for more upcoming features.

## Getting Started

```
$ git clone https://github.com/gafful/organa-android.git
$ cd organa-android
$ ./gradlew installDebug
```

Monitor logcat

```
$ adb logcat Organa:D *:S -b all -v color
```

## Running the tests

TODO

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

See also the list of [contributors](https://github.com/gafful/organa-android/contributors) who participated in this project.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/gafful/organa-android/tags). 

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Credits

* [Jaudiotagger](https://bitbucket.org/ijabz/jaudiotagger)
* [Lottie](https://github.com/airbnb/lottie-android)
* [Filechooser](https://github.com/hedzr/android-file-chooser)
* [Fuzzywuzzy](https://github.com/xdrop/fuzzywuzzy)