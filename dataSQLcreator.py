import json

def loadJSONfromFile(filename):
    # Ouvre JSON file
    # UnicodeDecodeError: 'charmap' codec can't decode byte 0x9d in position 29815: character maps to <undefined>
    # correction du bug -> encoding="utf-8"
    f = open(filename,"r",encoding="utf-8")

    data = json.load(f)

    # Ferme f file
    f.close()

    return data

def INSERT_INTO_SQLCreator_borne(data):
    SQLlistRequete = list()
    listID = list()

    for i in data:
        for j in i['Connections']:
            if j['ID'] not in listID:
                listID.append(j['ID'])

                IDBorne = str(j['ID'])
                IDStation = str(i['ID'])
                IDConnecteur = str(j['ConnectionTypeID'])

                try:
                    Puissance = str(j['PowerKW'])
                except:
                    Puissance = "-1"

                try:
                    idStatus = str(j['StatusTypeID'])
                except:
                    idStatus = "-1"

                try:
                    idCourant = str(j['CurrentTypeID'])
                except:
                    idCourant = "-1"

                requete = "INSERT INTO BorneToGo.Borne (idBorne, idStation, idConnecteur, idCourant, idStatus, Puissance) VALUES ('{}','{}','{}','{}','{}','{}');"

                SQLlistRequete.append(
                    requete.format(IDBorne, IDStation, IDConnecteur, idCourant, idStatus, Puissance)
                )

    return SQLlistRequete

def INSERT_INTO_SQLCreator_station(data):
    # Initialisation de la liste pour stocker les elements INSERT INTO
    SQLlistRequete = list()
    listID = list()

    for i in data:
        
        if i['ID'] not in listID:
            listID.append(i['ID'])

            IDStation = str(i['ID'])
            Latitude = str(i['AddressInfo']['Latitude'])
            Longitude = str(i['AddressInfo']['Longitude'])
            Adresse = correctionString(str(i['AddressInfo']['AddressLine1']))

            try:
                Ville = correctionString(str(i['AddressInfo']['Town']))
            except:
                Ville = "-1"
            
            try:
                Code_Postal = str(i['AddressInfo']['Postcode'])
            except:
                Code_Postal = "-1"

            Titre = correctionString(str(i['AddressInfo']['Title']))

            try:
                idPaiement = str(i['UsageTypeID'])
            except:
                idPaiement = "-1"
            
            requete = "INSERT INTO BorneToGo.Station (idStation, idPaiement, Titre, Latitude, Longitude, Adresse, Ville, CodePostal) VALUES ('{}','{}','{}','{}','{}','{}','{}','{}');"

            SQLlistRequete.append(
                requete.format(IDStation,idPaiement,Titre,Latitude,Longitude,Adresse,Ville,Code_Postal)
            )

    return SQLlistRequete

def INSERT_INTO_SQLCreator_connecteur(data):
    SQLlistRequete = list()
    listID = list()

    for i in data['ConnectionTypes']:
        if i['ID'] not in listID:
            IDConnecteur = i['ID']
            Titre = i['Title']
            Name = i['FormalName']

            requete = "INSERT INTO BorneToGo.Connecteur (idConnecteur, Titre, Name) VALUES ('{}','{}','{}');"

            SQLlistRequete.append(
                requete.format(IDConnecteur,Titre,Name)
            )

    return SQLlistRequete

def INSERT_INTO_SQLCreator_courant(data):
    SQLlistRequete = list()
    listID = list()

    for i in data['CurrentTypes']:
        if i['ID'] not in listID:
            IDConnecteur = i['ID']
            Titre = i['Title']
            Description = i['Description']

            requete = "INSERT INTO BorneToGo.Courant (idCourant, Titre, Description) VALUES ('{}','{}','{}');"

            SQLlistRequete.append(
                requete.format(IDConnecteur,Titre,Description)
            )

    return SQLlistRequete

def INSERT_INTO_SQLCreator_status(data):
    SQLlistRequete = list()
    listID = list()

    for i in data['StatusTypes']:
        if i['ID'] not in listID:
            IDConnecteur = i['ID']
            Titre = i['Title']
            IsOperational = i['IsOperational']
            IsUserSelectable = i['IsUserSelectable']

            requete = "INSERT INTO BorneToGo.Status (idStatus, Titre, IsOperational, IsUserSelectable) VALUES ('{}','{}','{}','{}');"

            SQLlistRequete.append(
                requete.format(IDConnecteur,Titre,IsOperational,IsUserSelectable)
            )

    return SQLlistRequete

def INSERT_INTO_SQLCreator_paiement(data):
    SQLlistRequete = list()
    listID = list()

    for i in data['UsageTypes']:
        if i['ID'] not in listID:
            IDConnecteur = i['ID']
            Titre = i['Title']
            IsPayAtLocation = i['IsPayAtLocation']
            IsMembershipRequired = i['IsMembershipRequired']
            IsAccessKeyRequired = i['IsAccessKeyRequired']

            requete = "INSERT INTO BorneToGo.Paiement (idPaiement, Titre, IsPayAtLocation, IsMembershipRequired, IsAccessKeyRequired) VALUES ('{}','{}','{}','{}','{}');"

            SQLlistRequete.append(
                requete.format(IDConnecteur,Titre,IsPayAtLocation,IsMembershipRequired,IsAccessKeyRequired)
            )

    return SQLlistRequete

def INSERT_INTO_SQLCreator_voiture(data):
    i33 = i25 = i1036 = 0
    for i in data:
        for j in i['Connections']:
            if j['ConnectionTypeID'] == 33:
                i33 += 1
            if j['ConnectionTypeID'] == 25:
                i25 += 1
            if j['ConnectionTypeID'] == 1036:
                i1036 += 1
    print("i33=",i33,", i25=",i25,", i1036=",i1036)

def createSQLFilefromlist(data,filename):
    fs = open(filename,"w",encoding="utf-8")

    # Every element of the list is write on one line
    for i in data:
        fs.write('\n%s' % i)

    fs.close()

def correctionString(texte):
    return texte.replace("'","\\'")

INSERT_INTO_SQLCreator_voiture(loadJSONfromFile("dataStationBorne.json"))

createSQLFilefromlist(INSERT_INTO_SQLCreator_borne(loadJSONfromFile("dataStationBorne.json")),"borne.sql")
createSQLFilefromlist(INSERT_INTO_SQLCreator_station(loadJSONfromFile("dataStationBorne.json")),"station.sql")

createSQLFilefromlist(INSERT_INTO_SQLCreator_connecteur(loadJSONfromFile("dataReference.json")),"connecteur.sql")
createSQLFilefromlist(INSERT_INTO_SQLCreator_courant(loadJSONfromFile("dataReference.json")),"courant.sql")
createSQLFilefromlist(INSERT_INTO_SQLCreator_status(loadJSONfromFile("dataReference.json")),"status.sql")
createSQLFilefromlist(INSERT_INTO_SQLCreator_paiement(loadJSONfromFile("dataReference.json")),"paiement.sql")