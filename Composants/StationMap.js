// Components/StationMap.js

import React, { PureComponent } from 'react'
import { StyleSheet, View, Text } from 'react-native'
import { Marker, Callout } from 'react-native-maps';
import { Svg, Image as ImageSvg } from 'react-native-svg'; // On utilise Svg car un bug de react native fait que les images de react-native ne s'affichent pas (sauf dans un <Text></Text> mais cela ajoute des marges et compliquent la mise en forme)
import BorneMap from '../Composants/BorneMap';


class StationMap extends PureComponent {

    constructor(props) {
        super(props);
    }

    render() {
        const { marker } = this.props;
        console.log(marker);
        return (
            <Marker
                coordinate={{
                    latitude: marker.latitude,
                    longitude: marker.longitude
                }}
                title={marker.adresse}
            >
                <Callout tooltip>
                    <View>
                        <View style={styles.bulle}>
                            <Text style={styles.name}>
                                {marker.adresse}
                            </Text>
                            <Text style={styles.description}>
                                {marker.bornes.map(item =>
                                    <BorneMap key={`borne-${marker.idStation}-${item.idBornes}`} borne={item} />
                                )}
                            </Text>
                            <View style={styles.image}>
                                <Svg width={110} height={110} >
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
            </Marker >
        );
    }
}

/* <Text style={styles.description}>{marker.description}</Text> */


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
    active: {
        borderColor: '#D83C54',
    },
})

export default StationMap