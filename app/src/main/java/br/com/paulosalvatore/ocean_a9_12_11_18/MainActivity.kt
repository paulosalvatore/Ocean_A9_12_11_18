package br.com.paulosalvatore.ocean_a9_12_11_18

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.net.URL

class MainActivity : AppCompatActivity() {

	companion object {
		private const val URL_IMAGEM =
			"https://i2.wp.com/mapinguanerd.com.br/wp-content/uploads/2017/02/curso-samsung-ocean-jogo-5.jpg"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		doAsync {
			val bitmap = loadImage(URL_IMAGEM)

			runOnUiThread {
				ivImagem.setImageBitmap(bitmap)
			}
		}

		btWorker.setOnClickListener {
			workerThread()
		}

		btAsync.setOnClickListener {
			asyncTask()
		}
	}

	private fun workerThread() {
		Toast.makeText(this, "Iniciando Worker Thread", Toast.LENGTH_LONG).show()

		ivImagem.setImageResource(android.R.color.transparent)

		Thread(Runnable {
			val bitmap = loadImage(URL_IMAGEM)

			ivImagem.post {
				ivImagem.setImageBitmap(bitmap)
			}
		}).start()
	}

	private fun loadImage(urlImagem: String) = try {
		val url = URL(urlImagem)
		val bitmap = BitmapFactory.decodeStream(
			url.openConnection().getInputStream()
		)

		bitmap
	} catch (e: Exception) {
		Log.e("MainActivity", "Erro ao carregar a imagem", e)
		null
	}

	private fun asyncTask() {
		Toast.makeText(this, "Iniciando Async Task", Toast.LENGTH_LONG).show()

		CarregarImagemTask().execute()
	}

	inner class CarregarImagemTask : AsyncTask<Void, Void, Bitmap?>() {
		override fun doInBackground(vararg p0: Void?) = loadImage(URL_IMAGEM)

		override fun onPreExecute() {
			ivImagem.setImageResource(android.R.color.transparent)
		}

		override fun onPostExecute(result: Bitmap?) {
			ivImagem.setImageBitmap(result)
		}
	}
}
