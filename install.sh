#!/usr/bin/env bash
git branch -d master
git checkout -b master
git mv baked/* .
git commit -am "Baked"
git push -u origin master -f

