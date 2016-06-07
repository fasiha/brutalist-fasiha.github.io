#!/bin/bash
fswatch -0 -r src | xargs -0 -n 1 -I {} planck build.cljs

