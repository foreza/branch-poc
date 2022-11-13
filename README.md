# branch-poc

## A small app that tests a few things:
- Dynamically creates some buttons that fire user events. (REGISTER / GIVE 5 STARS)
- On initialization, fires an event as well. (ACHIEVEMENT!) Haha!
- Generates a deep link (to itself, hah!) on open and adds it to the clipboard
- (Not working) Clicking the generated deep link takes you the site... so it's not finding the app. I must have missed something.
- But re-opening the app will then persist the data from the original deep link. (simulating an install in the case the app did not yet exist)


## What I learned
- Deep linking is cool; reading is hard.
- All bless the server(s) that power these redirects and fire-and-forget beacons.
- Making helper classes doesn't really help me, but it does make me feel good.
- The activity lifecycle does not forgive you easily



