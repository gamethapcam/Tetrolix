package com.tetrolix.game.screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.tetrolix.game.*
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.actors.txt
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actor
import ktx.scene2d.table

class LoadingScreen(private val context: Context) : KtxScreen {
    private val game = context.inject<KtxGame<KtxScreen>>()
    private val assets = context.inject<AssetManager>()
    private val stage = context.inject<Stage>()
    private val label = Label("Loading...", Scene2DSkin.defaultSkin, Labels.Huge()).apply {
        setAlignment(Align.center)
        setWrap(true)
    }
    private var finishedLoading = false

    override fun show() {
        MusicAssets.values().forEach { assets.load(it) }
        SoundAssets.values().forEach { assets.load(it) }

        stage.clear()
        stage.addActor(table {
            // expand label to entire screen so that the user can click anywhere
            actor(label) { cell -> cell.expand().fill() }.onClick {
                if (finishedLoading) {
                    game.removeScreen<LoadingScreen>()
                    this@LoadingScreen.dispose()

                    game.addScreen(GameScreen(context))
                    game.addScreen(HighscoreScreen(context))
                    game.addScreen(MenuScreen(context))
                    game.addScreen(SelectLevelScreen(context))

                    game.setScreen<MenuScreen>()
                }
            }
            setFillParent(true)
        })
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun render(delta: Float) {
        if (assets.update() && !finishedLoading) {
            finishedLoading = true
            context.bindSingleton(AudioManager(context.inject(), context.inject()))
            label.txt = "Touch to continue"
            label += forever(alpha(0f) + fadeIn(1f) + delay(0.25f) + fadeOut(1f))
        }

        stage.act(delta)
        stage.viewport.apply(true)
        stage.draw()
    }
}