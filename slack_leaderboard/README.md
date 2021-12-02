# Advent of Code - Slack Leaderboard

This project publish the leaderboard everyday in a Slack channel

## Configuration
1. Create a Firebase project (Blaze level required to do outgress)
2. Install the Firebase CLI: - https://firebase.google.com/docs/cli to update the configuration
3. Login with the CLI: `firebase login` (`firebase login:ci` to get a token)
4. Configuration:
   After the configuration `firebase functions:config:get` should be: 
   ``` json
   {
  "adventofcode": {
    "url": "https://adventofcode.com/2021/leaderboard/private/view/<id>", // the id can be found in the leaderboard path
    "session": "<SESSION_COOKIE>" // the value of your session cookie on Advent of Code
  },
  "slack": {
    "channel": "#advent_of_code", // name of the channel
    "bot_token": "<token>", // can be found in the Slack bot configuration
    "incominghook": "https://hooks.slack.com/services/...", // can be found in the Slack bot configuration
    "incominghook_dev": "https://hooks.slack.com/services/...", // can be found in the Slack bot configuration
    "signing_secret": "<signing_secret>",  // can be found in the Slack bot configuration
    "app_id": "<app_id>", // can be found in the Slack bot configuration
    "verification_token": "<verification_token>" // can be found in the Slack bot configuration
  }
}
   ```
   To configure:
   - `firebase functions:config:set adventofcode.url=<value>`
   - `firebase functions:config:set adventofcode.session=<value>`
   - `firebase functions:config:set slack.bot_token=<value>`
   - etc 

To deploy:
`./scripts/deploy.sh`