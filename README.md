# Personal static website

## Setup and build instructions

### Setup
1. `brew install planck`—other Clojure/Script environments are certainly possible but have not yet been tested. For now, Mac-only (brew/Planck are Mac-only).
1. `mkdir website && cd website`—this directory will contain two sub-directories, one tracking the `dev` branch (source, build script, etc.), and another tracking `master` branch (static website). `website` isn't important, you can make this whatever you want.
1. `git clone <this repo> dev`
1. `cd dev`—even this directory name is arbitrary, you can make it whatever you want. It'll store the `dev` branch.
1. `git checkout master && git checkout dev`—pull down the remote `master` branch to local git. We won't do anything with it, we just want it locally.
1. `mkdir ../master`—this directory name is hardcoded into `build.cljs`. If you want the `master` branch to live in another directory, make sure you edit `build.cljs` and update the `output-directory` definition.
1. `git worktree add ../master master`

> (Huge thanks to [@krisajenkins](https://twitter.com/krisajenkins)' post, [Git for Static Sites](http://blog.jenkster.com/2016/02/git-for-static-sites.html)! The instructions above differ from Kris' because, while `gh-pages` is the branch for your *project* sites, Github requires *user* sites like [fasiha.github.io](fasiha.github.io) to have static content in `master`. See [Github Pages](https://pages.github.com/) for juicy details.)

### Build (convert source to static HTML)
To re-build the website (in `website/master`), make sure you're in `dev` directory, then
```
$ planck build.cljs
```
This will rebuild all sources and put it in `../master`. Commit both directories/branches, `git push --all` in either of the directories, and your changes will go live in a few seconds!

### Watch files and rebuild
Again in `dev/` directory, invoke the following: whenever any HTML file changes, the build script is rerun. (Inefficient, I know…)
```
$ brew install fswatch
$ fswatch -0 -r src | xargs -0 -n 1 -I {} planck build.cljs
```

## Notes

### Vim
For HTML, I use one space per indentation and an effectively-infinite textwidth. In Vim,
```
:set tabstop=1
:set shiftwidth=1
:set tw=123123
```
