	Compilation:
Le projet est d�coup� en 4 petits projets qui ont des d�pendances: Client et Serveur n�cessitent les classes de Common.
Generateur n�cessite les classes de Client (mais n�est pas essentiel, cette application ne sert qu�� g�n�rer des fichiers de param�tres pour pr�remplir les informations serveur et ne pas avoir � laisser les utilisateurs finaux les remplir lors de leur premi�re utilisation).

Les 3 main() sont client.Application, serveur.Serveur et Generateur (qui est optionnel).


	Utilisation de l�application de clavardage :
Ex�cuter le serveur sur une machine (attention comme beaucoup d�applications tournant sur des serveurs il fonctionne en invisible et n�est pas facilement refermable si non lanc� � partir d�une console)
Ex�cuter le client sur au moins 2 autres machines. Si un des 2 clients est ex�cut� sur la machine serveur, il ne faut pas entrer �localhost� comme adresse serveur, mais l�adresse IP classique.
Clavarder � travers l�interface graphique

	Test sur une seule machine:
Si pour une raison ou une autre il est plus simple d�effectuer les tests sur une seule machine, lancer le serveur et un client, puis utiliser le fichier client_SECONDAIRE_TEST.jar se trouvant sur le git pour lancer un 2eme client. Ce client est un peu obsol�te (et l�ve quelques exceptions qu�il ne devrait pas) mais il se comporte correctement envers le client primaire et n�entre pas en conflit avec lui et suffit pour faire des v�rifications.

	Ex�cution du programme :
Serveur : Lancer le programme, aucun argument n�est n�cessaire, il suffira de remplir le port � utiliser lorsque ce sera demand�.
Client : Lancer le programme, aucun argument n�est n�cessaire, il faudra donner l�adresse du serveur et son port (que vous aurez rentr� lors de son lancement). Puis � la fen�tre de connexion, choisir votre pseudonyme puis se connecter. Une fois connect� il suffit de choisir une personne connect�e dans la liste de contact (en vert) puis lui envoyer un message. Pour changer de pseudonyme, il suffit (sans fermer la fen�tre principale), de changer le nom de la fen�tre de connexion puis cliquer � nouveau sur connexion.

	Remarques :
Sauvegardes des conversations et du param�trage serveur dans des fichiers (1 par conversation) sur la machine de l�utilisateur. Le fichier de param�trage sera g�n�r� dans le m�me dossier que l�ex�cution, les fichiers de conversation dans un sous dossier �ConvDB�.
Les utilisateurs sont identifi�s par un UUID (identifiant unique al�atoire g�n�r� � leur premi�re utilisation) et non par leur pseudo ou adresse afin d�associer un utilisateur a sa machine comme requis dans le cahier des charges. Il peut donc changer de pseudo ou d�adresse � volont� et toujours �tre reconnu par les autres.
