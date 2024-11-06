import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.caverock.androidsvg.SVG
import com.example.moneymanager.R
import com.example.moneymanager.data.model.CategoryData
import com.example.moneymanager.databinding.ItemCategoryBinding
import java.io.InputStream

class SelectIncomeExpenseAdapter(
    private var listCategory: List<CategoryData.Category>,
    private var clickRadioButtonIconCategory : (CategoryData.Category) -> Unit
) :
    RecyclerView.Adapter<SelectIncomeExpenseAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryData.Category) {
            binding.tvCategoryName.text = category.name
            loadSvg(binding.ivIcon, category.icon)
            if(category.isCheck == false){
                binding.ivRadioButton.setImageResource(R.drawable.radio_button_no)
            }else{
                binding.ivRadioButton.setImageResource(R.drawable.radio_button_yes)
            }

            binding.ivRadioButton.setOnClickListener {
                clickRadioButtonIconCategory(category)
                if(category.isCheck == false){
                    binding.ivRadioButton.setImageResource(R.drawable.radio_button_yes)
                }
            }
        }

        private fun loadSvg(imageView: ImageView, assetFileName: String) {
            try {
                val assetManager = imageView.context.assets
                val inputStream: InputStream = assetManager.open(assetFileName)
                val svg = SVG.getFromInputStream(inputStream)
                val drawable = PictureDrawable(svg.renderToPicture())
                imageView.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
                imageView.setImageDrawable(drawable)
                inputStream.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCategory[position])
    }

    fun updateData(listCategory: List<CategoryData.Category>){
        this.listCategory = listCategory
        notifyDataSetChanged()
    }

}
