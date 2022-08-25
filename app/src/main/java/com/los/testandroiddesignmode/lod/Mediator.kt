package com.los.testandroiddesignmode.lod

import android.util.Log


private const val TAG = "Los:Mediator"

class Mediator {

    private val mRooms = ArrayList<Room>()

    init {
        mRooms.apply {
            add(Room(14F, 23F))
            add(Room(141F, 231F))
            add(Room(142F, 232F))
            add(Room(143F, 234F))
            add(Room(144F, 233F))
            add(Room(145F, 235F))
        }
    }

    fun rentOut(roomArea: Float, roomPrice: Float): Room? {

        val rooms = getRooms()
        for (room in rooms) {
            if (isSuitable(roomArea, roomPrice, room)) {
                Log.d(TAG, "rentRoom: 成功租到房子")
                return room
            }
        }
        return null
    }


    private fun getRooms(): ArrayList<Room> {
        return mRooms
    }

    private fun isSuitable(roomArea: Float, roomPrice: Float, room: Room): Boolean {
        return room.area > roomArea && room.price < roomPrice
    }


}