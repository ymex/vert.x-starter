package cn.ymex.starter.model

import cn.ymex.starter.core.AbstractModel

object ModelRegister {
  val models = mutableListOf<AbstractModel<*, *>>(ScoreModel())
}
