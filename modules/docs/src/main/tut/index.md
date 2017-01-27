---
layout: home
technologies:
 - first: ["Android", "Android SDK"]
 - second: ["Scala", "We are using Scala On Android"]
 - third: ["Open Source", "By opening its development, we hope that the community helps us to expand and improve this project"]
---

**FOR USERS**

- **It's easier to find apps when they are grouped in Collections**

9 Cards categorizes your apps into smart Collections. You can also explore Collections created and shared by other users in the community and discover new ways to use your smartphone.

- **Every moment of the day is different using Moments**

Your phone use changes throughout the day, that’s why 9 Cards shows widgets by Moments that are adapted to your daily routine. Whether you like to play games when you’re at home, listen to music on your commute, or check email during work, 9 Cards will learn your habits and launch the apps you want, when you want them.

- **Apps are important, your Contacts are too**

Our App Drawer's behavior is focused on your productivity. With a simple gesture, you can access your contacts, filter your apps by recently installed, or see your missed calls. You can also search directly in Google Play. 


FOR DEVELOPERS

- **Functional Programing in the client side**

The 9 Cards app was developed with Scala on Android and emphasizes a Functional Programming style over the available imperative Android SDK. 9 Cards doesn’t use common Java Android libraries for views; instead, all views are based on Macroid for Functional UI composition and the business logic in the client is built atop a simple monad transformer stack in Scala. 

- **A modern architecture in the Backend using Free Monads**

The server app architecture is based on free monads. A Free Monad is a data type that allows the decoupling of program declaration from program interpretation. This means you can use Free monads and Free applicatives to completely describe a program’s business logic.

- **9 Cards would not be possible without Scala Libraries**

We are using some of the most popular Scala libraries in the client and backend, for instance: Cats, Monix, Doobie, scalaZ, scalacheck and so on, and frameworks such as Spray on the server side. Our goal has been to create a similar architecture for the client and server using these amazing libraries of the Scala Ecosystem.