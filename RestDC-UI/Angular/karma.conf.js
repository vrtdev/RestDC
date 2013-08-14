// Karma configuration
// Generated on Thu May 23 2013 07:08:47 GMT+0200 (CEST)
module.exports = function (karma) {
    karma.set({


            // base path, that will be used to resolve files and exclude
            basePath: '.',

            plugins: [
                // these plugins will be require() by Karma
                'karma-jasmine',
                'karma-chrome-launcher',
                'karma-coverage',
                "karma-junit-reporter",
                "karma-phantomjs-launcher"
            ],

            frameworks: ["jasmine"],
            // list of files / patterns to load in the browser
            files: [
                'src/main/js/*.js',
                'src/test/lib/angular-mocks.js',
                'src/test/spec/*.js'
            ],

            preprocessors: {
                'src/main/js/app.js': 'coverage'
            },


            // list of files to exclude
            exclude: [
                // 'app/lib/**'
            ],


            // test results reporter to use
            // possible values: 'dots', 'progress', 'junit'
            reporters: ['progress', 'coverage', 'dots', 'junit'],


            // web server port
            port: 9876,


            // cli runner port
            runnerPort: 9100,


            // enable / disable colors in the output (reporters and logs)
            colors: true,


            // level of logging
            // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
            logLevel: karma.LOG_INFO,


            // enable / disable watching file and executing tests whenever any file changes
            autoWatch: false,


            // Start these browsers, currently available:
            // - Chrome
            // - ChromeCanary
            // - Firefox
            // - Opera
            // - Safari (only Mac)
            // - PhantomJS
            // - IE (only Windows)
            browsers: [
                //'Chrome',
                'PhantomJS'
            ],


            // If browser does not capture in given timeout [ms], kill it
            captureTimeout: 60000,


            // Continuous Integration mode
            // if true, it capture browsers, run tests and exit
            singleRun: true,
            junitReporter: {
                outputFile: 'target/test-results.xml'
            },

            coverageReporter: {
                //type: 'html',
                type: 'lcov',
                dir: 'target/coverage/'

            }

        }
    )
}
;

