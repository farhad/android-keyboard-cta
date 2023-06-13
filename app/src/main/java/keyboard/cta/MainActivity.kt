package keyboard.cta

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import keyboard.cta.databinding.ActivityMainBinding
import keyboard.cta.databinding.ItemOneBinding
import keyboard.cta.databinding.ItemThreeBinding
import keyboard.cta.databinding.ItemTwoBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var adapter = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpLayout()
    }

    private fun setUpLayout() {
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            isNestedScrollingEnabled = true
        }
        binding.recyclerview.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val list = listOf(
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.THREE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.THREE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.THREE),
            GeneralDataItem(type = ViewType.TWO, showCta = false),
            GeneralDataItem(type = ViewType.THREE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.ONE)
        )

        adapter.submitList(list)
    }

}



data class GeneralDataItem(val showCta: Boolean = false, val type: ViewType)

class MyAdapter : ListAdapter<GeneralDataItem,GeneralViewHolder>(ItemDiffUtil) {
    companion object {
        private object ItemDiffUtil : DiffUtil.ItemCallback<GeneralDataItem>() {
            override fun areItemsTheSame(oldItem: GeneralDataItem, newItem: GeneralDataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GeneralDataItem, newItem: GeneralDataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun getItemViewType(position: Int): Int = currentList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralViewHolder {
        return when(ViewType.from(viewType)) {
            ViewType.ONE -> {
                ItemOneViewHolder(ItemOneBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }

            ViewType.TWO -> {
                ItemTwoViewHolder(ItemTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }

            ViewType.THREE -> {
                ItemThreeViewHolder(ItemThreeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            }
        }
    }

    override fun onBindViewHolder(holder: GeneralViewHolder, position: Int) {
        holder.bind(currentList[holder.adapterPosition])
    }
}

enum class ViewType {
    ONE,
    TWO,
    THREE;

    companion object {
        fun from(value: Int): ViewType {
            return values()[value]
        }
    }
}


abstract class GeneralViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: GeneralDataItem)
}


class ItemOneViewHolder(val binding: ItemOneBinding) : GeneralViewHolder(binding.root) {
    override fun bind(item: GeneralDataItem) {

    }
}

class ItemTwoViewHolder(val binding: ItemTwoBinding) : GeneralViewHolder(binding.root) {

    override fun bind(item: GeneralDataItem) {
        binding.chipCta.visibility = if (item.showCta) View.VISIBLE else View.GONE
    }
}

class ItemThreeViewHolder(val binding: ItemThreeBinding) : GeneralViewHolder(binding.root) {
    override fun bind(item: GeneralDataItem) {

    }
}