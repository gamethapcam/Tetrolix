package com.tetrolix.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context

class MenuScreen(context: Context) : KtxScreen {
    private val game = context.inject<KtxGame<KtxScreen>>()
    private val stage = context.inject<Stage>()

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun render(delta: Float) {
        if (Gdx.input.isTouched) {
            game.setScreen<GameScreen>()
        }

        stage.act(delta)
        stage.viewport.apply(true)
        stage.draw()
    }
}