#!/usr/bin/env node

const path = require("path"),
      cp = require("child_process");

var cmd = `docker run -d --name bwp-pma --network bwp-net -e PMA_HOST=bwp-mysql -p 8080:80 phpmyadmin/phpmyadmin`;

cp.execSync(cmd);