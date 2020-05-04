#!/usr/bin/env node

const path = require("path"),
      cp = require("child_process");

var cmd = `docker stop bwp-mysql && docker rm bwp-mysql`;

cp.execSync(cmd);