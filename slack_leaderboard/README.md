# Advent of Code - Slack Leaderboard

This project publish the leaderboard everyday in a Slack channel

## Configuration
1. Create a Firebase project (Blaze level required to do scheduled function)
    Needed features:
    - Realtime database (read: false, write: false)
    - Functions
    - Pub/Sub (for scheduled functions): https://firebase.google.com/docs/functions/schedule-functions
  
2. Install the Firebase CLI: - https://firebase.google.com/docs/cli to update the configuration
   
3. Edit `.firebaserc` with your project name
   
4. Login with the CLI: `firebase login` (`firebase login:ci` to get a token)
   
5. Configuration:
   After the configuration `firebase functions:config:get` should be: 
``` (json)
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

## Slack bot configuration
Import the following manifest and replace `<firebase-project-baseurl>` by the your functions base url (in Firebase / Functions): 
``` yaml
_metadata:
  major_version: 1
  minor_version: 1
display_information:
  name: Advent of Code - Leaderboard
features:
  app_home:
    home_tab_enabled: true
    messages_tab_enabled: false
    messages_tab_read_only_enabled: true
  bot_user:
    display_name: Advent of Code - Leaderboard
    always_online: true
oauth_config:
  scopes:
    bot:
      - incoming-webhook
settings:
  event_subscriptions:
    request_url: https://<firebase-project-baseurl>.cloudfunctions.net/slackEvents
    bot_events:
      - app_home_opened
  interactivity:
    is_enabled: true
    request_url: https://<firebase-project-baseurl>.cloudfunctions.net/slackPayloads
  org_deploy_enabled: false
  socket_mode_enabled: false
  token_rotation_enabled: false

```

## Testing locally

https://firebase.google.com/docs/functions/local-shell

Example: 
1. `firebase functions:config:get > .runtimeconfig.json`
2. `export GOOGLE_APPLICATION_CREDENTIALS="path-to-json-key"`
3. - `firebase functions:shell`
4. - `firebase > scheduledFetchLeaderboard()`

## References

- Slack bot token: https://api.slack.com/authentication/token-types#bot
- Slack requests verification: https://api.slack.com/authentication/verifying-requests-from-slack
-  Firebase CLI: https://firebase.google.com/docs/cli
-  Firebase Functions: https://firebase.google.com/docs/reference/functions