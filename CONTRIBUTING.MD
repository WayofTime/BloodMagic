# Contributing to Blood Magic
## Workflow
1. It is in your best interest to join the [Blood Magic Discord](https://discord.gg/VtNrGrs) and have a discussion about it with us. The #dev-environment channel (and the threads in there) are the best place for this. This way you dont end up coding something we dont feel like it belongs in Blood Magic, or that someone else is already working on the same thing
2. Fork this repository and clone your fork locally
3. Open the project in your favourite IDE or Text Editor and make sure to sync the gradle project
4. Add this repository as upstream by running `git remote add upstream git@github.com:WayofTime/BloodMagic.git` and make sure your local clone knows about all the branches of it by running `git fetch upstream` after.
5. Create a new local branch based on the upstream branch you wish to target (eg `git checkout -b <your branch name> upstream/1.21.1`)
6. Make your changes in your new branch.
7. Test your changes both on client only and client - server setups. We will still test it ourselves as more brains usually means covering more dumb stuff that could be done and might not work as intended
8. Now you can commit your changes, push them to your fork (`git push --set-upstream <your branch name>`) and open a pull request
9. We will review and test your changes and may ask you to change some things. If we do, change them on your local branch, commit and push it again (--set-upstream not needed anymore) and let us know we did so, so we can repeat as needed.

If at any point you are unsure about anything related like how or where to put something, dont be afraid to ask about it on the discord
Be aware that we may not always have time immediately. We all have a real life to attend to and sometimes that takes too much time

There isnt much to add for a Styleguide, essentially "just follow whats already there" and if you're not sure ask on discord about it.

Heres a list of some things that have been mentioned before:
- Registry Keys, Resource Locations, Translation keys and all other sorts of strings that refer to something in code are kept in `snake_case`