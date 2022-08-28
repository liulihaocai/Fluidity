package me.liuli.fluidity.util.world

import me.liuli.fluidity.util.mc
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.Vec3
import kotlin.math.cos
import kotlin.math.sin

fun raycastEntity(range: Double, yaw: Float, pitch: Float, entityFilter: (Entity) -> Boolean): Entity? {
    val renderViewEntity = mc.renderViewEntity

    if (renderViewEntity != null && mc.theWorld != null) {
        var blockReachDistance = range
        val eyePosition = renderViewEntity.getPositionEyes(1f)

        val yawCos = cos(-yaw * 0.017453292f - Math.PI.toFloat())
        val yawSin = sin(-yaw * 0.017453292f - Math.PI.toFloat())
        val pitchCos = (-cos(-pitch * 0.017453292f.toDouble())).toFloat()
        val pitchSin = sin(-pitch * 0.017453292f.toDouble()).toFloat()

        val entityLook = Vec3((yawSin * pitchCos).toDouble(), pitchSin.toDouble(), (yawCos * pitchCos).toDouble())
        val vector = eyePosition.addVector(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance)
        val entityList = mc.theWorld!!.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.entityBoundingBox.addCoord(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance).expand(1.0, 1.0, 1.0)) {
            it != null && (it !is EntityPlayer || !it.isSpectator) && it.canBeCollidedWith()
        }

        var pointedEntity: Entity? = null

        for (entity in entityList) {
            if (!entityFilter(entity))
                continue

            val collisionBorderSize = entity.collisionBorderSize.toDouble()
            val axisAlignedBB = entity.entityBoundingBox.expand(collisionBorderSize, collisionBorderSize, collisionBorderSize)

            val movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector)

            if (axisAlignedBB.isVecInside(eyePosition)) {
                if (blockReachDistance >= 0.0) {
                    pointedEntity = entity
                    blockReachDistance = 0.0
                }
            } else if (movingObjectPosition != null) {
                val eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec)

                if (eyeDistance < blockReachDistance || blockReachDistance == 0.0) {
                    if (entity == renderViewEntity.ridingEntity && !renderViewEntity.canRiderInteract()) {
                        if (blockReachDistance == 0.0)
                            pointedEntity = entity
                    } else {
                        pointedEntity = entity
                        blockReachDistance = eyeDistance
                    }
                }
            }
        }

        return pointedEntity
    }

    return null
}