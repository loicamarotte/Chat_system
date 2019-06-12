	Compilation:
Le projet est découpé en 4 petits projets qui ont des dépendances: Client et Serveur nécessitent les classes de Common.
Generateur nécessite les classes de Client (mais n’est pas essentiel, cette application ne sert qu’à générer des fichiers de paramètres pour préremplir les informations serveur et ne pas avoir à laisser les utilisateurs finaux les remplir lors de leur première utilisation).

Les 3 main() sont client.Application, serveur.Serveur et Generateur (qui est optionnel).


	Utilisation de l’application de clavardage :
Exécuter le serveur sur une machine (attention comme beaucoup d’applications tournant sur des serveurs il fonctionne en invisible et n’est pas facilement refermable si non lancé à partir d’une console)
Exécuter le client sur au moins 2 autres machines. Si un des 2 clients est exécuté sur la machine serveur, il ne faut pas entrer “localhost” comme adresse serveur, mais l’adresse IP classique.
Clavarder à travers l’interface graphique

	Test sur une seule machine:
Si pour une raison ou une autre il est plus simple d’effectuer les tests sur une seule machine, lancer le serveur et un client, puis utiliser le fichier client_SECONDAIRE_TEST.jar se trouvant sur le git pour lancer un 2eme client. Ce client est un peu obsolète (et lève quelques exceptions qu’il ne devrait pas) mais il se comporte correctement envers le client primaire et n’entre pas en conflit avec lui et suffit pour faire des vérifications.

	Exécution du programme :
Serveur : Lancer le programme, aucun argument n’est nécessaire, il suffira de remplir le port à utiliser lorsque ce sera demandé.
Client : Lancer le programme, aucun argument n’est nécessaire, il faudra donner l’adresse du serveur et son port (que vous aurez rentré lors de son lancement). Puis à la fenêtre de connexion, choisir votre pseudonyme puis se connecter. Une fois connecté il suffit de choisir une personne connectée dans la liste de contact (en vert) puis lui envoyer un message. Pour changer de pseudonyme, il suffit (sans fermer la fenêtre principale), de changer le nom de la fenêtre de connexion puis cliquer à nouveau sur connexion.

	Remarques :
Sauvegardes des conversations et du paramétrage serveur dans des fichiers (1 par conversation) sur la machine de l’utilisateur. Le fichier de paramétrage sera généré dans le même dossier que l’exécution, les fichiers de conversation dans un sous dossier “ConvDB”.
Les utilisateurs sont identifiés par un UUID (identifiant unique aléatoire généré à leur première utilisation) et non par leur pseudo ou adresse afin d’associer un utilisateur a sa machine comme requis dans le cahier des charges. Il peut donc changer de pseudo ou d’adresse à volonté et toujours être reconnu par les autres.
