#!/usr/bin/env node

const path = require("path"),
      cp = require("child_process");

var cmd = `docker stop bwp-pma && docker rm bwp-pma`;

cp.execSync(cmd);