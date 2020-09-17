# situm-android-module-capacitycontrol
Count people in geofences &amp; trigger capacity control alerts with Situm

## Content
1. [Capacity Control module](https://github.com/situmtech/situm-android-module-capacitycontrol/tree/master/CapacityControl) - Library module that offers Capacity Control functionality. 
2. [Example app](https://github.com/situmtech/situm-android-module-capacitycontrol/tree/master/app) - Provides an example on how to implement the CapacityControl module with very basic functionality.

## Getting started
To use this module, you must have a Situm account configured with a building.

To control an area's capacity you must define a Geofence wich covers the area and define the maximum capacity in a custom field:

>**max_capacity** = number of people

The module offers a warning capacity functionality. The warning capacity must be lower than the maximum capacity and is set to 80% of the maximum capacity by default.

This functionality is enabled with the option [CapacityControlConfiguration.warningCapacityActive()](https://github.com/situmtech/situm-android-module-capacitycontrol/blob/a14e04a956b9932ae86f4ac9f117e60c55f9ab47/CapacityControl/src/main/java/es/situm/integration/CapacityControlConfiguration.kt#L17).

You can also configure the warning capacity value with a custom field:

>**waning_capacity_threshold** = number of people

1. [Register a Situm account and create your building](https://situm.freshdesk.com/support/solutions/folders/35000214359)
2. [Create Geofences inside the building](https://situm.freshdesk.com/support/solutions/articles/35000132829-how-to-use-the-geofencing-tool-)
3. [Define custom fields inside your geofence](https://situm.freshdesk.com/support/solutions/articles/35000087693-how-to-use-the-custom-fields)

## Implementation details
The module has 2 interfaces which must be implemented and provided to the [CapacityControlIntegrator](https://github.com/situmtech/situm-android-module-capacitycontrol/blob/master/CapacityControl/src/main/java/es/situm/integration/CapacityControlIntegrator.kt).

* [Configuration interface](https://github.com/situmtech/situm-android-module-capacitycontrol/blob/master/CapacityControl/src/main/java/es/situm/integration/CapacityControlConfiguration.kt): This interfaces handles the module's configuration.
* [Callback interface](https://github.com/situmtech/situm-android-module-capacitycontrol/blob/master/CapacityControl/src/main/java/es/situm/integration/CapacityControlListener.kt) This interface receives the events detected by the module.

The module also requires the app to provide it with location and realtime data by using the [CapacityControlInput](https://github.com/situmtech/situm-android-module-capacitycontrol/blob/master/CapacityControl/src/main/java/es/situm/integration/CapacityControlInput.kt) object.