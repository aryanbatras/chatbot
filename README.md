
Firstly, I took a deep dive into the Whatsapp cloud API, for which I had to sign up to Meta for Developers, set up a permanent token, and verify a template helloworld message through POST.

Then, I had to start the spring boot project with spring web and firebase dependencies, and then I tested GET request at /api/health point, and soon after that, I configured template helloworld POST requests to Whatsapp Cloud API through Spring Boot REST API.

Then, I had to figure out a way, to make whatsapp chatbot reply back automatically, for which I need to provide a url to meta (webhook) where meta sends back meta information whenever a user sends a message to the bot and initiates the conversation. For that, I used ngrok (to connect local host end point to internet) and exposed my app on the internet, and I provided the same URL to meta webhook through which I was able to send "Okay, we'll get back to you shortly" messages.

But this doesn't look convinent for an end user, so I added Cloudflare Workers AI into it, with a free tier, using an open source AI from their cloud with unlimited bandwidth and usage, (alternative platforms like OpenAI has no free tier API).

Through AI, I was able to give user messages as prompt to it, and the message it sent back was provided to the end user.
However, there were many challenges along the way, the AI was not working for complex prompts and it took me 3 hours to figure out that the issue was with the whatsapp context window limits, when the AI responded with large text (more than 1500 characters approximately), whatsapp blocked sending it, but I fixed it by limiting the maximum response upto 1000 characters.

Another challenge was I had to immediately respond back to the Meta webhook about the message being received (return from the webhook function), and initiate a reply from it at the same time. There were some issues like duplicate messages which I had solved using Asynchronous method and immediately responded to meta.

After this, I created jaruratcare-db firebase database, and connected it to spring boot using firebase java sdk by reading docs, I tested it on spring boot locally on my device, where the end point was exposed through ngrok and connected to meta webhook, it was working and storing user number, message, and timestamp perfectly.

Then I had to deploy this to Render.com, and it required a docker container to run the spring boot, used the openjdk-17 image, wrote the basic docker file to build through maven and deploy executable jar. I was able to deploy it after some tries, and it worked perfectly.

But I had faced a major issue in production, " Invalid JWT Authentication " for firebase, it took me 3 to 4 hours again to figure out the root cause, which was that the json file storing the api keys in the /resources folder was not accepted by google. Google restricts storing apis like these for its services, that is firebase, because of this, in production, my api was disabled.

To fix this, I generated a new firebase key and never stored the json file in project, and instead used Environment Variables on Render.com, and entered the json file as base64 single string encoded format to avoid any newline errors or whitespace errors, which I decoded and parsed to successfully authenticate in Render.com.

Finally, It was a great experience building this project. It is almost production ready. It just needs a business phone number to be used as the number for bot, which will help us customize the bot name and logo. And to make our bot live, we need to provide some details to the meta, such as privacy policy, terms of service.

- Aryan