#!/bin/bash
/usr/local/bin/npm config set proxy http://proxy.vrt.be:8080
/usr/local/bin/npm install
karma start --single-run
