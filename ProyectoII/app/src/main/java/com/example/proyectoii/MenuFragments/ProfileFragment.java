package com.example.proyectoii.MenuFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< Updated upstream
=======
import android.widget.TextView;
>>>>>>> Stashed changes

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectoii.ImageAdapter;
<<<<<<< Updated upstream
import com.example.proyectoii.R;

import java.util.Objects;
=======
import com.example.proyectoii.Objetos.Educacion;
import com.example.proyectoii.R;
>>>>>>> Stashed changes

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
<<<<<<< Updated upstream
=======
        TextView textView = view.findViewById(R.id.nameTxt);
        textView.setText(String.format("%s %s", usuario.getNombre(), usuario.getApellido()));

        StringBuilder userInfo = new StringBuilder(String.format("%s %s", "Fecha de nacimiento:", usuario.getFechaNac()));
        userInfo.append("\n");
        userInfo.append(String.format("%s %s", "Ciudad:", usuario.getCiudad()));
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append("\n");
        userInfo.append(String.format("%s %s", "Contacto:", usuario.getTelefono()));

        if (usuario.getEducacicion() != null)
        for (Educacion education: usuario.getEducacicion())
        {
            userInfo.append("\n");
            userInfo.append(String.format("%s %s", String.format("%s:", education.getTipoInstitucion()), education.getNombreInstitucion()));
        }
        TextView infoTxt = view.findViewById(R.id.infoTxt);

        infoTxt.setText(userInfo.toString());
>>>>>>> Stashed changes

        ViewPager viewPager =  view.findViewById(R.id.view_pager);
        String[] images = {"https://images.unsplash.com/photo-1535498730771-e735b998cd64?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80",
                "https://images.unsplash.com/photo-1535498730771-e735b998cd64?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80"};
        ImageAdapter adapter = new ImageAdapter(this.getContext(), images);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount()-1);

        return view;
    }
}
