#!/usr/bin/env node

const path = require("path"),
      cp = require("child_process");

var cmd = `docker run -d --name bwp-mysql --network bwp-net -e MYSQL_ROOT_PASSWORD=bwp-mysql -p 3306:3306 -v ${path.resolve(process.cwd(), "backend/db")}:/var/lib/mysql mysql`;

cp.execSync(cmd);