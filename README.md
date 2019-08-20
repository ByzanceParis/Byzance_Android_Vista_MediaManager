# Byzance_Android_Vista_MediaManager

Android Vista Media Manager est un projet d'une librairie Android permettant l'update des contenus des médias d'un projet par le biais d'une API. A terme cette librairie pourra aussi être utilisée pour remonter de la data des projets.


## Fonctionalités Actuelles 

check la date de la dernière update.  
Si elle est différente de celle qu'elle a, recupère la liste des medias.  
check si chaque média a été ajouté, modifié ou effacé et met a jour  
sauve la nouvelle date d'update  
Notifie que l'update est terminée

## Intégrer la librairie
L'intégration de la librairie a été faite via Jitpack en utilisant
[cet article](https://medium.com/@anujguptawork/how-to-create-your-own-android-library-and-publish-it-750e0f7481bf) comme référence.


Pour intégrer la librairie il suffit d'ajouter dans build.gradle (projet)
```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

puis remplacer le Tag par la version de la librairie voulue et l'ajouter dans le build.gradle (du module app)
```
	implementation 'com.github.ByzanceParis:Byzance_Android_Vista_MediaManager:Tag'
```

On peut ensuite utiliser la librairie dans l'application.

## Exemple d'utilisation

Dans l'application utilisant la librairie créer
-  un singleton [VistaManager](https://github.com/ByzanceParis/Byzance_Android_Vista_MediaManager/blob/master/app/src/main/java/world/byzance/vista_test/VistaManager.java) qui gerera Vista.
- une custom [Application](https://github.com/ByzanceParis/Byzance_Android_Vista_MediaManager/blob/master/app/src/main/java/world/byzance/vista_test/MyApplication.java) pour avoir un context global pour l'app. (ne pas oublier de l'ajouter au [manifest](https://github.com/ByzanceParis/Byzance_Android_Vista_MediaManager/blob/master/app/src/main/AndroidManifest.xml))
- Créer une [activity](https://github.com/ByzanceParis/Byzance_Android_Vista_MediaManager/blob/master/app/src/main/java/world/byzance/vista_test/MainActivity.java) se lançant au début de l'app, dédiée à l'update qui pourra à terme afficher des informations relatives à l'update et lancer le reste de l'app une fois l'update terminée.


Exemple de l'intégration de l'api dans l'application des [Heures de Cartier](https://github.com/ByzanceParis/Cartier_Android_HeuresdeParfum/tree/VistaApi)