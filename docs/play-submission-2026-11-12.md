# Google Play submission record — 2026-11-12

App: 墨メモ / app.sumimemo.offline
Developer: Cription (6473303972136501152)
Play app ID: 4974693149761428960
Dashboard: https://play.google.com/console/u/0/developers/6473303972136501152/app/4974693149761428960/app-dashboard

Completed:
- Created the Play app after owner confirmation of the app-creation declarations.
- Saved privacy-policy URL: https://msandroid.github.io/sumi-memo/ (HTTP 200 verified).
- Published policy via GitHub Pages from codex/android-release-preparation /docs.
- Added owner-provided public support email, exactly Criotion@proton.me.
- Added an in-app link to the hosted privacy policy. Rebuilt signed AAB; 2 emulator tests and lint passed again; bundletool validation passed.
- Saved login-unrestricted, no-ads, no-government, no-financial-functions, no-health-functions declarations.
- Saved target audience 13–15, 16–17 and 18+; data safety declares no collection or sharing.
- After explicit owner consent to IARC terms, completed rating questionnaire. Current ratings: ESRB Everyone, PEGI 3, IARC Generic 3+; Console shows IARC complete.
- Saved category Productivity and support website https://msandroid.github.io/sumi-memo/.
- Saved Japanese store text as a draft. Graphics not uploaded yet.
- Selected Japan for Closed testing Alpha track 4700956270513493742.

Console gate:
Production access requires a published closed test and at least 12 opted-in testers maintained continuously for 14 days. Console shows 0 opted-in testers and production access application is disabled. This cannot be substituted by local automated tests.

Current upload blocker:
Chrome extension fileChooser.setFiles rejects uploads with a file-URL-access setting error, including after the owner reported enabling it and a new Play tab was tried. Asked owner to verify the Inference profile and reload the extension, or upload the four prepared images manually. Browser navigation to chrome://extensions was rejected by the tool URL policy; no workaround of that setting was attempted.

Pending:
- Receive real tester Google-account addresses or Google Group from owner.
- Upload icon, feature graphic and two screenshots from docs/store/assets, finalize listing.
- Upload latest signed AAB app/build/outputs/bundle/release/app-release.aab.
- Complete Alpha release preparation, any Play signing declaration, then review submission.
- Conduct actual 12-person / 14-day closed test, apply for production access, obtain Google approval, submit production and verify public availability.

Latest signed AAB SHA256: B41D326A3E66EE8D4C40EB963570BBFFD194FF84FCEB8DCB5E516B69378F9AB8

No app binary uploaded, test release published, review submitted, or production publication yet at this checkpoint.

## Later checkpoint
- Created Alpha release draft /releases/1/prepare and entered name 1.0.0 — 初回クローズドテスト and Japanese release notes. Saving remains disabled until an AAB is attached.
- Feedback email was entered as Criotion@proton.me. Console requires a tester mailing list before the tester setup task can complete; owner was asked for actual Google-account participants or a Google Group.
- Uploaded four public store assets to the connected Google Drive via its upload_file tool as an alternative to local browser file transfer. File IDs: icon 1Jvr1qbfBIVrX4bhavzEWO7On4HyRwuPS; feature 1A6ck2DS596YkmDADEc-h5xvkZWh7lKuy; home 1dczPVnIIQte0KjA4HHJb5s4OS4u8j8N9; editor 1qSQl8agmhUufqwK1JhMNckYWME53u5uu.
- Play's Add from Drive opened a Google account chooser. Selecting the existing account timed out twice at the browser input layer; no new OAuth permission was accepted.
- Owner reported enabling file URL access, but fileChooser.setFiles still returned the same rejection, including on a fresh Play tab. Asked owner to verify the Inference Chrome profile and reload the extension, or manually upload graphics.
- Actual store screenshots were recaptured at 1080x1920 and visually checked.
