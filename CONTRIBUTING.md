# Contributing to BloodMagic

First off, thank you for taking the time to contribute!

## Issues

First, make sure the issue has not been already reported. Have a look through the open issues on GitHub tagged with the version of Minecraft that you're playing with. (Also check the closed issues if you aren't playing with the latest version of the mod - your issue may have already been resolved in an update.)

If it has not been reported, follow the provided issue template (created when you click "New Issue"). Please be sure to fill in your Blood Magic version! If you're playing with other mods, see if you can re-create the issue with just Blood Magic and its dependencies. If not, see if you can figure out exactly which mods are required to trigger the issue, and list them too.

If it has been reported, provide any additional information you can to the current active issue. 

## Pull Requests

With the ~~1.8~~ 1.21 rewrite comes a need to keep the code *clean*. Pull Requests will be looked over a bit stricter from now on.

When you wish to contribute, please keep these points in mind:

1) This guide assumes you are familiar with the basics of Java, Git, NeoForge, and modding minecraft in general. If not, there are plenty of tutorials out there, tailored to every level of expertise and method of learning; familiarise yourself with these tools before continuing.
2) Before anything else, talk to us! Either open a PR with the basis of your idea or, for a more informal chat, come join our [Discord](https://discord.gg/VtNrGrs). The #dev-environment channel (and the threads in there) are the best place for this. That way we can discuss alternatives, bring up balance concerns, talk about whether it sounds like it'd make more sense as an addon-mod, etc. 
3) Once we've hammered out a decent and well-balanced design (or at least 'good enough for now'), then you can start making PRs (See below). After that we can refine, test, balance, refractor etc.
4) Keep in mind that this process may not be fast - we all have lives outside of Blood Magic and sometimes things can take us away from the mod. If you ignore this and start coding before discussing it, you may find yourself having to throw away a lot of work.

### Making PRs
1) Fork this repository and clone your fork locally.
2) Open the project in your favourite IDE or Text Editor and make sure to sync the gradle project.
3) Add this repository as upstream by running `git remote add upstream git@github.com:WayofTime/BloodMagic.git` and make sure your local clone knows about all the branches of it by running `git fetch upstream` after.
4) Create a new local branch based on the upstream branch you wish to target (eg `git checkout -b <your branch name> upstream/1.21.1`)
5) Make your changes in your new branch.
6) Test your changes both on client only and client - server setups. We'll still test it ourselves, for completeness' sake.
7) Now you can commit your changes, push them to your fork (`git push --set-upstream <your branch name>`) and open a pull request.
8) We will review and test your changes and may ask you to change some things. If we do, change them on your local branch, commit and push it again (--set-upstream not needed anymore) and let us know you've done so, so we can repeat as needed.

### Do:
* Try and make each commit represent a meaningful change to the code.
  * Squash extra commits if it's reasonable to do so.
* Use meaningful names for classes, functions and parameters. 
  * Code should be self documenting and the intents should be immediately clear. If in doubt, elaborate in a comment.
* Describe each and every change you make in your Pull Request.
    * This lets everybody know exactly what is going on for easy discussion.
* Make short yet descriptive commit titles.
    * Feel free to give a very basic overview of the commit in the message, then use the description to go into detail.
* Keep your formatting the same as the project. 
    * see [STYLE_GUIDE.MD](/STYLE_GUIDE.md) for more details

### Do not:
* Make unnecessary changes to files.
    * If you don't need to touch it, don't touch it. 
    * This includes: *renaming args*, *renaming files*, *editing formatting*, etc.


## Human-Driven Development
Blood Magic is a labour of love and has been hand-coded for over a decade,  and we want to keep it that way. No-one on the dev team has any interest in agentic programming or AI-Driven development, and reading Claude code is frankly exhausting. 

As such, we politely request that any PRs should be written by *you*, not merely prompted into existence. If code is written strangely or doesn't fit in with the rest of the mod's style, we will have questions. 

This also applies to artwork - There are plenty of talented artists looking for gigs and if we decide to get more imagery for the mod, we'll commission them. 

## If in doubt, ask. If dangerously certain, ask anyway.
If a little cursory research on GitHub and discord haven't provided a conclusive answer, then there's no harm in asking. We'd much rather answer a few up-front questions than deny a 500-line PR, so please, always get clarification if there's the slightest seed of doubt. Similarly, push your code often and ask people to double-check it - many eyes make light work. (this also makes our jobs easier!)