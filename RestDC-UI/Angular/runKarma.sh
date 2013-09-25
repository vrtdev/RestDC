#!/bin/bash
/usr/local/bin/npm config set proxy http://proxy.vrt.be:8080
/usr/local/bin/npm install
./node_modules/karma/bin/karma start --single-run
