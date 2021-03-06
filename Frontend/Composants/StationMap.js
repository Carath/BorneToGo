// Components/StationMap.js

import React, { PureComponent } from 'react'
import { StyleSheet, View, Text } from 'react-native'
import { Marker, Callout } from 'react-native-maps';
import { Svg, Image as ImageSvg } from 'react-native-svg';
import { connect } from 'react-redux';


class StationMap extends PureComponent {

    constructor(props) {
        super(props);
    }

    // Méthode pour modifier la station active
    changerStationActive(id) {
        const TIME_FOR_ANIMATION = 0;
        const action = { type: 'BORNE_ACTIVE_MODIFIEE', value: id }
        this.props.dispatch(action);
        setTimeout(() => {
            this.markerRef.showCallout();
        }, TIME_FOR_ANIMATION);
    }

    // Méthode pour afficher les bornes disponibles
    renderBornesDisponibles(nbTotal, nbDispo) {
        return (
            (nbDispo == 0) ?
                <Text style={styles.pasdispo}>Pas de bornes disponibles</Text> :
                (nbDispo > 1) ? <Text style={styles.dispo}>{nbDispo}/{nbTotal} disponibles</Text> :
                    <Text style={styles.dispo}>{nbDispo}/{nbTotal} disponible</Text>
        )
    }

    // Méthode pour afficher le callout
    renderCalloutMarker(propsnavigation, marker, nbTotal, nbDispo) {

        return (
            <Callout
                tooltip
                onPress={() => propsnavigation.navigation.navigate('Station', {
                    station: marker,
                })}
            >
                <View>
                    <View style={styles.bulle}>
                        <Text style={styles.name}>
                            {marker.title}
                        </Text>
                        <Text style={styles.description}>
                            <View style={styles.bornesDispo}>
                                {this.renderBornesDisponibles(nbTotal, nbDispo)}
                            </View>
                        </Text>
                        <View style={styles.image}>
                            <Svg width={100} height={100} >
                                <ImageSvg
                                    width="100%"
                                    height="100%"
                                    preserveAspectRatio="xMidYMid slice"
                                    href={require('../Images/borne.png')}
                                />
                            </Svg>
                        </View>
                    </View>
                    <View style={styles.arrowborder} />
                    <View style={styles.arrow} />
                </View>
            </Callout>
        )
    }

    render() {
        const { marker, propsnavigation } = this.props;

        // On compte le nombre de bornes disponibles pour la station
        var nbTotal = Object.keys(marker.bornes).length;
        var nbDispo = 0;
        for (const obj of marker.bornes) {
            if (obj.status)
                nbDispo++;
        }

        // On regarde si la station est la station active
        var egalite = false;
        if (this.props.active == marker.idStation) {
            egalite = true;
        }
        else {
            egalite = false;
        }

        return (
            <Marker
                coordinate={{
                    latitude: marker.latitude ? marker.latitude : 0,
                    longitude: marker.longitude ? marker.longitude : 0,
                }}
                ref={(ref) => this.markerRef = ref}
                title={marker.adresse}
                pinColor={'green'}
                key={`${marker.idStation}`}
            >
                { this.renderCalloutMarker(propsnavigation, marker, nbTotal, nbDispo)}
            </Marker >
        );
    }
}


const styles = StyleSheet.create({
    // Bulle d'information
    bulle: {
        flexDirection: 'column',
        alignSelf: 'center',
        backgroundColor: '#fff',
        borderRadius: 6,
        borderColor: '#ccc',
        borderWidth: 0.5,
        padding: 15,
        width: 200,
    },
    // Affichage des informations
    name: {
        fontSize: 16,
        marginBottom: 5,
        fontWeight: 'bold',
        textAlign: 'center',
    },
    description: {
        fontSize: 14,
        fontStyle: 'italic',
        textAlign: 'center',
        marginBottom: 5,
    },
    bornesDispo: {
        justifyContent: 'center',
    },
    dispo: {
        fontSize: 14,
        color: 'green',
    },
    pasdispo: {
        fontSize: 14,
        color: 'red',
    },
    // Flèche sous la bulle
    arrow: {
        backgroundColor: 'transparent',
        borderColor: 'transparent',
        borderTopColor: '#fff',
        borderWidth: 16,
        alignSelf: 'center',
        marginTop: -32,
    },
    arrowborder: {
        backgroundColor: 'transparent',
        borderColor: 'transparent',
        borderTopColor: '#007a87',
        borderWidth: 16,
        alignSelf: 'center',
        marginTop: -0.5,
    },
    image: {
        alignItems: 'center',
        justifyContent: 'center',
    },
})

/* REDUX */
// Connexion du state global au component PageMap
const mapStateToProps = (state) => {
    return {
        active: state.borneActive.active
    }
}

// Dispatcher
const mapDispatchToProps = (dispatch) => {
    return {
        dispatch: (action) => { dispatch(action) }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(StationMap)