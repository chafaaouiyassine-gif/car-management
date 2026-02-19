-Etape 1 : Lancer le docker compose
docker compose -f docker-compose.yml up

--------------------------------------------------------------------
-Etape 2 : lancer l'application
java -jar target/garage-miscroservice-0.0.1-SNAPSHOT.jar

Note : Vérifier que vous avez JAVA 21
-------------------------------------------------------------------
-Etape 3 :
se connecter sur : http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------
-Etape 4 :
              * créer un garage
              * créer une vehicule
              * créer un accessoire


Note: les Json de test se trouve dans test/resources

