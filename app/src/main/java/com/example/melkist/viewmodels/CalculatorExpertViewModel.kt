package com.example.melkist.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.melkist.utils.ONE_MILLION
import com.example.melkist.utils.getPersianYear
import kotlin.math.pow

const val NO_ELEVATOR_VALUE_FIRST_FLOOR = 1.0/90.0
const val NO_ELEVATOR_VALUE_SECOND_FLOOR = 1.0/80
const val NO_ELEVATOR_VALUE_THIRD_FLOOR = 1.0/70
const val NO_ELEVATOR_VALUE_FORTH_FLOOR = 1.0/60
const val NO_ELEVATOR_VALUE_FIFTH_AND_MORE_FLOOR = 1.0/50
const val MORE_THAN_18_UNIT = 1.0/60
const val ALLAY_LESS_THAN_4_METER = 1.0/40
const val NORTHERN_UNIT = 1.0/60
const val SIZE_MORE_THAN_75_METER_WITH_ONE_ROOM = 1.0/80
const val UNDERNEATH_COMMERCIAL_UNIT = 1.0/60
const val NO_PARKING_VALUE_WITH_AGE_1_TO_7 = 12.5
const val NO_PARKING_VALUE_WITH_AGE_8_TO_11 = 9
const val NO_PARKING_VALUE_WITH_AGE_12_AND_MORE = 5

class CalculatorExpertViewModel: ViewModel() {

    // Expert values //
    var housePrice = 0.0
    var housePricePerMeter = 0.0

    var maxNewPropertyValue = /*0L*/ 80000000L
    var minNewPropertyValue = /*0L*/ 70000000L
    var buildYear = /*0*/ 1395
    var room = ""
    var roomNo = /*0*/ 3
    var size = /*0*/ 137
    var floor = ""
    var floorNo = /*0*/ 4
    var units = /*""*/ "qwe"
    var isMoreThan18units = false
    var allay = /*""*/ "aa"
    var isAllayWidthMoreThan4 = false
    var direction = /*""*/ "as"
    var isNorthern =  false
    var isUnlock = /*false*/ true
    var isHasElevator = /*false*/ true
    var isHasParking = /*false*/ true
    var isHasCommercialUnit = false

    // should calculate
    var isMoreThan75AndHasOneRoom = false

    fun showItemLogs() {
        Log.e("TAG", "showItems: \n" +
                "    var maxNewPropertyValue = $maxNewPropertyValue\n" +
                "    var minNewPropertyValue = $minNewPropertyValue\n" +
                "    var age = $buildYear\n" +
                "    var room = $room\n" +
                "    var isMoreThan75AndHasOneRoom = $isMoreThan75AndHasOneRoom\n" +
                "    var size = $size\n" +
                "    var floor = $floor\n" +
                "    var floorNo = $floorNo\n" +
                "    var units = $units" +
                "    var isMoreThan18units = $isMoreThan18units\n" +
                "    var allay = $allay\n" +
                "    var isAllayWidthMoreThan4 = $isAllayWidthMoreThan4\n"  +
                "    var direction = $direction\n" +
                "    var isNorthern = $isNorthern\n" +
                "    var unlock = $isUnlock\n" +
                "    var elevator = $isHasElevator\n" +
                "    var parking = $isHasParking\n" +
                "    var commercial = $isHasCommercialUnit\n", )
    }

    fun isAllFieldsOkay(): Boolean {
        val isNewValues = isNewValues()
        val isAge = buildYear > 0
        val isSize = size > 0
        val isRoom = room.isNotEmpty()
        val isFloor = floor.isNotEmpty()
        val isUnits = units.isNotEmpty()
        val isAllay = allay.isNotEmpty()
        val isDirection = direction.isNotEmpty()
        return isNewValues && isAge && isSize && isRoom && isFloor && isUnits && isAllay && isDirection
    }

    private fun isNewValues (): Boolean {
        return maxNewPropertyValue != 0L && minNewPropertyValue != 0L && maxNewPropertyValue >= minNewPropertyValue
    }

    fun calculatePrice() {
        val normPropertyValue = (maxNewPropertyValue + minNewPropertyValue) / 2
        Log.e("TAG", "calculatePrice: normPropertyValue $normPropertyValue ", )
        val normPValueInMillion = normPropertyValue / ONE_MILLION
        Log.e("TAG", "calculatePrice: normPValueInMillion $normPValueInMillion ", )
        val isUnlockPrice = normPValueInMillion * (if (isUnlock) 0.95 else 1.0)
        Log.e("TAG", "calculatePrice: isUnlockPrice $isUnlockPrice ", )
        val isUnlockPriceTenth = isUnlockPrice * 10
        Log.e("TAG", "calculatePrice: isUnlockPriceTenth $isUnlockPriceTenth ", )
        val isUnlockPriceTenthLength = isUnlockPriceTenth.toInt().toString().length
        Log.e("TAG", "calculatePrice: isUnlockPriceTenthLength $isUnlockPriceTenthLength ", )
        val value = 10.0.pow((isUnlockPriceTenthLength - 1).toDouble())
        Log.e("TAG", "calculatePrice: value $value ", )
        val upperIsUnlockPriceTenth = isUnlockPriceTenth + value
        Log.e("TAG", "calculatePrice: UpperIsUnlockPriceTenth $upperIsUnlockPriceTenth ", )
        val baseValue = upperIsUnlockPriceTenth * (getPersianYear() - buildYear)
        Log.e("TAG", "calculatePrice: baseValue $baseValue ", )
        val basePrice = isUnlockPrice - (baseValue / 1000)
        Log.e("TAG", "calculatePrice: basePrice $basePrice ", )
        val pricePerMeterRaw = basePrice - (basePrice * (
                    noElevatorSubtraction(floorNo) +
                            moreThan18UnitsSubtraction() +
                            allayWidthMoreThan4Subtraction() +
                            northernUnitSubtraction() +
                            underneathCommercialUnitSubtraction() +
                            moreThan75MeterWithOneRoomSubtraction(size, roomNo)
                ))
        Log.e("TAG", "calculatePrice: pricePerMeterRaw $pricePerMeterRaw ", )
        val totalPriceRaw = pricePerMeterRaw * size * ONE_MILLION
        Log.e("TAG", "calculatePrice: totalPriceRaw $totalPriceRaw ", )
        housePrice = totalPriceRaw - (basePrice * noParkingSubtraction(getPersianYear() - buildYear))
        Log.e("TAG", "calculatePrice: housePrice $housePrice ", )
        housePricePerMeter = housePrice / size
        Log.e("TAG", "calculatePrice: housePricePerMeter $housePricePerMeter ", )
    }

    private fun northernUnitSubtraction(): Double {
        return if (isNorthern)
            NORTHERN_UNIT
        else
            0.0
    }

    private fun allayWidthMoreThan4Subtraction(): Double {
        return if (!isAllayWidthMoreThan4)
            ALLAY_LESS_THAN_4_METER
        else
            0.0
    }

    private fun noElevatorSubtraction (floorNo: Int): Double {
        var value = 0.0
        if (!isHasElevator)
            value = when {
                floorNo == 1 -> NO_ELEVATOR_VALUE_FIRST_FLOOR
                floorNo == 2 -> NO_ELEVATOR_VALUE_SECOND_FLOOR
                floorNo == 3 -> NO_ELEVATOR_VALUE_THIRD_FLOOR
                floorNo == 4 -> NO_ELEVATOR_VALUE_FORTH_FLOOR
                floorNo >= 5 -> NO_ELEVATOR_VALUE_FIFTH_AND_MORE_FLOOR
                else -> 0.0
            }
        return value
    }

    private fun moreThan18UnitsSubtraction(): Double {
        return if (isMoreThan18units)
            MORE_THAN_18_UNIT
        else
            0.0
    }


    private fun underneathCommercialUnitSubtraction(): Double {
        return if (isHasCommercialUnit)
            UNDERNEATH_COMMERCIAL_UNIT
        else
            0.0
    }

    private fun moreThan75MeterWithOneRoomSubtraction(size: Int, roomNo: Int): Double {
        return if (size < 75 && roomNo <= 1){
            isMoreThan75AndHasOneRoom = true
            SIZE_MORE_THAN_75_METER_WITH_ONE_ROOM
        }else {
            isMoreThan75AndHasOneRoom = false
            0.0
        }
    }

    private fun noParkingSubtraction (age: Int): Double {
        return if (!isHasParking)
            when (getPersianYear() - age){
                in 0..7 -> NO_PARKING_VALUE_WITH_AGE_1_TO_7
                in 8..11 -> NO_PARKING_VALUE_WITH_AGE_8_TO_11.toDouble()
                else -> NO_PARKING_VALUE_WITH_AGE_12_AND_MORE.toDouble()
            }
        else
            0.0
    }

}